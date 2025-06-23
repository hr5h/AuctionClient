package com.example.auctionclient.data.services

import com.example.auctionclient.data.entities.LotApi
import com.example.auctionclient.domain.Lot
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LotListService {

    @GET("lots/")
    suspend fun getLots(@Header("Authorization") token: String): List<Lot>

    @GET("lots/{username}/won")
    suspend fun getWinningLots(@Path("username") username: String, @Header("Authorization") token: String): List<Lot>

    @POST("lots/")
    suspend fun createLot(@Header("Authorization") token: String, @Body lotApi: LotApi)
}