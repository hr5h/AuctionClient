package com.example.auctionclient.presentation.lot_detail

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lotId = savedStateHandle.get<Long>("lotId") ?: 0L

    val lot = Lot(
        id = lotId,
        title = "Name$lotId",
        description = "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription",
        startPrice = 1000f,
        currentPrice = 1500f,
        status = "Open",
        ownerId = 1,
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
}