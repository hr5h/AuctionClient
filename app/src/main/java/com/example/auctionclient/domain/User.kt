package com.example.auctionclient.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val username: String,
)
