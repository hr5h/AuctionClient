package com.example.auctionclient.presentation.lot_list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.domain.Lot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotListViewModel @Inject constructor() : ViewModel() {

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
}