package com.example.auctionclient.presentation.lot_detail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.data.repo.LotDetailRepository
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import com.example.auctionclient.domain.Owner
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val lotDetailRepository: LotDetailRepository
) : ViewModel() {

    private val _lotDetailState: MutableStateFlow<LotDetailState> =
        MutableStateFlow(LotDetailState())
    val lotDetailState: StateFlow<LotDetailState> = _lotDetailState.asStateFlow()

    private val _bidState: MutableStateFlow<BidState> = MutableStateFlow(BidState())
    val bidState: StateFlow<BidState> = _bidState.asStateFlow()

    private var isSubscribe: Boolean = false
    val username = lotDetailRepository.getUserName()

    val lot: MutableStateFlow<Lot> = MutableStateFlow(
        savedStateHandle.get<String>("lot")?.let {
            Json.decodeFromString<Lot>(it)
        } ?: Lot(
            id = 0,
            title = "Not Found",
            description = "Lot not found",
            startPrice = 0f,
            currentPrice = 0f,
            status = "UNKNOWN",
            owner = Owner(0, "null"),
            endTime = ""
        )
    )


    val bids = mutableStateListOf<Bid>()

    init {
        viewModelScope.launch {
            if (!isSubscribe) {
                subscribeLot()
            }
            getBids()
        }
    }

    fun showBid(isShowBid: Boolean) {
        _lotDetailState.update { it.copy(showBid = isShowBid) }
    }

    fun changeAmountBid(value: String) {
        _bidState.update { it.copy(amount = value) }
    }

    fun changeSelected(value: BidAmount) {
        _bidState.update { it.copy(selected = value) }
        when (value) {
            BidAmount.Min -> changeAmountBid((lot.value.currentPrice + 10).roundToInt().toString())
            BidAmount.Amount10 -> changeAmountBid(
                (lot.value.currentPrice * 1.1).roundToInt().toString()
            )

            BidAmount.Amount20 -> changeAmountBid(
                (lot.value.currentPrice * 1.2).roundToInt().toString()
            )

            BidAmount.Amount30 -> changeAmountBid(
                (lot.value.currentPrice * 1.3).roundToInt().toString()
            )

            BidAmount.Amount40 -> changeAmountBid(
                (lot.value.currentPrice * 1.4).roundToInt().toString()
            )

            BidAmount.Amount50 -> changeAmountBid(
                (lot.value.currentPrice * 1.5).roundToInt().toString()
            )

            BidAmount.Amount100 -> changeAmountBid(
                (lot.value.currentPrice * 2).roundToInt().toString()
            )
        }
    }

    fun submitBid() {
        if (_bidState.value.amount != "") {
            viewModelScope.launch {
                lotDetailRepository.createBid(lot.value.id, _bidState.value.amount.toLong())
                _bidState.update { BidState() }
                showBid(false)
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Ставка принята", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Ставка не принята", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getBids() {
        bids.clear()
        bids.addAll(lotDetailRepository.getBids(lot.value.id))
    }

    private suspend fun subscribeLot() {
        isSubscribe = true
        lotDetailRepository.subscribeLot(lot.value.id) { newLot ->
            lot.update {
                viewModelScope.launch {
                    getBids()
                }
                newLot
            }
        }
    }

    fun finalizeLot() {
        viewModelScope.launch {
            lotDetailRepository.finalizeLot(lot.value.id)
        }
    }

    fun closeLot() {
        viewModelScope.launch {
            lotDetailRepository.closeLot(lot.value.id)
        }
    }
}