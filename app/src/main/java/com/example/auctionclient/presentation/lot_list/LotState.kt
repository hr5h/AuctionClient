package com.example.auctionclient.presentation.lot_list

data class LotState(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val startPrice: Float = 0f,
    val endTime: Float = 30f,
)
