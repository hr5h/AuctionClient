package com.example.auctionclient.data.services

import com.example.auctionclient.data.entities.LoginRequest
import com.example.auctionclient.data.entities.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: LoginRequest): LoginResponse
}