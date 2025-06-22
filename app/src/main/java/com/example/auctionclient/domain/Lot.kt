package com.example.auctionclient.domain

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Lot(
    val id: Long,
    val title: String,
    val description: String,
    val startPrice: Float,
    val currentPrice: Float,
    val status: String = "Open",
    val owner: Owner,
    val endTime: String?,
)