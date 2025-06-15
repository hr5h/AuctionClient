package com.example.auctionclient.presentation.lot_list

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.data.repo.LotListRepository
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.domain.Lot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stompClient: StompClient,
    private val lotListRepository: LotListRepository
) : ViewModel() {

    private val _lotListState: MutableStateFlow<LotListState> = MutableStateFlow(LotListState())
    val lotListState: StateFlow<LotListState> = _lotListState.asStateFlow()

    private val _lotState: MutableStateFlow<LotState> = MutableStateFlow(LotState())
    val lotState: StateFlow<LotState> = _lotState.asStateFlow()

    val lots = mutableStateListOf<Lot>()

    init {
        viewModelScope.launch {
            lots.addAll(lotListRepository.getLots())
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
        if (_lotState.value.title != "" &&
            _lotState.value.description != "" &&
            _lotState.value.imageUrl != "" &&
            _lotState.value.startPrice > 0f &&
            _lotState.value.endTime > 0f
        ) {
            viewModelScope.launch {
                lotListRepository.createLot(
                    _lotState.value.title,
                    _lotState.value.description,
                    _lotState.value.startPrice
                )
                _lotState.update { LotState() }
                showDialogLot(false)
                launch(Dispatchers.Main) {
                    Toast.makeText(context, "Лот добавлен", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Лот не добавлен", Toast.LENGTH_SHORT).show()
        }
    }
}