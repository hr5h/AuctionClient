package com.example.auctionclient.data.services

import com.example.auctionclient.domain.Bid
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BidService {

    @GET("lots/{lotId}/bids")
    suspend fun getBids(
        @Path("lotId") lotId: Long,
        @Header("Authorization") token: String
    ): List<Bid>

    @POST("lots/{lotId}/finalize")
    suspend fun finalizeLot(
        @Path("lotId") lotId: Long,
        @Header("Authorization") token: String
    )
}