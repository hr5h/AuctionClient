package com.example.auctionclient.domain

import java.time.Instant

data class Bid(
    val amount: Int,
    val bidder: Owner,
    val lot: Lot,
    val timeStamp: Instant,
)