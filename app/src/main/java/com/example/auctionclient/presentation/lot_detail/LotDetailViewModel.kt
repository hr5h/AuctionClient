package com.example.auctionclient.presentation.lot_detail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import com.example.auctionclient.domain.Owner
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val stompClient: StompClient
) : ViewModel() {

    private val _lotDetailState: MutableStateFlow<LotDetailState> =
        MutableStateFlow(LotDetailState())
    val lotDetailState: StateFlow<LotDetailState> = _lotDetailState.asStateFlow()

    private val _bidState: MutableStateFlow<BidState> = MutableStateFlow(BidState())
    val bidState: StateFlow<BidState> = _bidState.asStateFlow()

    private val lotId = savedStateHandle.get<Long>("lotId") ?: 0L

    val lot = Lot(
        id = lotId,
        title = "Name$lotId",
        description = "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription",
        startPrice = 1000f,
        currentPrice = 1500f,
        status = "Open",
        owner = Owner(0),
        endTime = 10f
    )

    val bids = mutableStateListOf<Bid>()

    init {
        viewModelScope.launch {
            repeat(10) { ind ->
                bids.add(
                    Bid(
                        amount = 500,
                        bidderId = ind.toLong(),
                        lotId = Random.nextLong(1, 5),
                        timeStamp = "10:23"
                    )
                )
            }
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
            BidAmount.Min -> changeAmountBid((lot.currentPrice + 10).roundToInt().toString())
            BidAmount.Amount10 -> changeAmountBid((lot.currentPrice * 1.1).roundToInt().toString())
            BidAmount.Amount20 -> changeAmountBid((lot.currentPrice * 1.2).roundToInt().toString())
            BidAmount.Amount30 -> changeAmountBid((lot.currentPrice * 1.3).roundToInt().toString())
            BidAmount.Amount40 -> changeAmountBid((lot.currentPrice * 1.4).roundToInt().toString())
            BidAmount.Amount50 -> changeAmountBid((lot.currentPrice * 1.5).roundToInt().toString())
            BidAmount.Amount100 -> changeAmountBid((lot.currentPrice * 2).roundToInt().toString())
        }
    }

    fun submitBid() {
        //TODO Bid
        if (_bidState.value.amount != "") {
            _bidState.update { BidState() }
            showBid(false)
            Toast.makeText(context, "Ставка принята", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Ставка не принята", Toast.LENGTH_SHORT).show()
        }
    }
}