package com.example.auctionclient.domain

import kotlinx.serialization.Serializable

@Serializable
data class Lot(
    val id: Long,
    val title: String,
    val description: String,
    val startPrice: Float,
    val currentPrice: Float,
    val status: String = "Open",
    val owner: Owner,
    val endTime: Float?,
)