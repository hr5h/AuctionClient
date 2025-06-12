package com.example.auctionclient.presentation.lot_list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.domain.Lot
import com.example.auctionclient.presentation.lot_detail.BidState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotListViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _lotListState: MutableStateFlow<LotListState> = MutableStateFlow(LotListState())
    val lotListState: StateFlow<LotListState> = _lotListState.asStateFlow()

    private val _lotState: MutableStateFlow<LotState> = MutableStateFlow(LotState())
    val lotState: StateFlow<LotState> = _lotState.asStateFlow()

    val lots = mutableStateListOf<Lot>()

    init {
        viewModelScope.launch {
            repeat(10) { ind ->
                delay(1000)
                lots.add(
                    Lot(
                        id = ind.toLong(),
                        title = "Name$ind",
                        description = "Description",
                        startPrice = 1000f,
                        currentPrice = 1500f,
                        status = "Open",
                        ownerId = 1,
                        endTime = 10f + ind.toFloat() * 10
                    )
                )
            }
        }
    }

    fun showDialogLot(isShow: Boolean) {
        _lotListState.update { it.copy(showDialogLot = isShow) }
    }

    fun changeTitle(value: String) {
        _lotState.update { it.copy(title = value) }
    }

    fun changeDescription(value: String) {
        _lotState.update { it.copy(description = value) }
    }

    fun changeImageUrl(value: String) {
        _lotState.update { it.copy(imageUrl = value) }
    }

    fun changeStartPrice(value: String) {
        _lotState.update { it.copy(startPrice = value.toFloat()) }
    }

    fun changeEndTime(value: String) {
        _lotState.update { it.copy(endTime = value.toFloat()) }
    }

    fun submitLot() {
        //TODO Lot
        if (_lotState.value.title != "" ||
            _lotState.value.description != "" ||
            _lotState.value.imageUrl != "" ||
            _lotState.value.startPrice < 0f ||
            _lotState.value.endTime < 0f) {
            _lotState.update { LotState() }
            showDialogLot(false)
            Toast.makeText(context, "Лот добавлен", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Лот не добавлен", Toast.LENGTH_SHORT).show()
        }
    }
}