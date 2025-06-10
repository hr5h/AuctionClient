package com.example.auctionclient.domain

data class Lot(
    val id: Long,
    val title: String,
    val description: String,
    val startPrice: Float,
    val currentPrice: Float,
    val status: String = "Open",
    val ownerId: Long,
    val endTime: Float,
)