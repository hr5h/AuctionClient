package com.example.auctionclient.domain

data class Bid(
    val amount: Int,
    val bidder: Owner,
    val lot: Lot,
    val timestamp: String,
)