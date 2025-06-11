package com.example.auctionclient.presentation.lot_detail

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import kotlinx.coroutines.launch
import kotlin.random.Random

class LotDetailViewModel(): ViewModel() {

    val lot = Lot(
        id = 1,
        title = "Name1",
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