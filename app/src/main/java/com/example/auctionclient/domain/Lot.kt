package com.example.auctionclient.domain

import kotlinx.serialization.Serializable

@Serializable
data class Lot(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val startPrice: Float,
    val currentPrice: Float,
    val status: String = "Open",
    val owner: User,
    val winner: User?,
    val endTime: String?,
)