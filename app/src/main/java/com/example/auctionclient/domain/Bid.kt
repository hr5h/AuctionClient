package com.example.auctionclient.domain

data class Bid(
    val amount: Int,
    val bidder: User,
    val lot: Lot,
    val timestamp: String,
)