package com.example.auctionclient.domain

data class Bid(
    val amount: Int,
    val bidderId: Long,
    val lotId: Long,
    val timeStamp: String,
)