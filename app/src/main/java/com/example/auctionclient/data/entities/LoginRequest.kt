package com.example.auctionclient.data.entities

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val login: String,
    val password: String
)
