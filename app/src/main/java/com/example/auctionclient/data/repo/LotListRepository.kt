package com.example.auctionclient.data.repo

import com.example.auctionclient.data.entities.LotApi
import com.example.auctionclient.data.services.LotListService
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.data.utils.InternetChecker
import com.google.gson.JsonArray
import javax.inject.Inject

interface LotListRepository {

    suspend fun getLots(): JsonArray
    suspend fun createLot(title: String, description: String, startPrice: Float)
}

class LotListRepositoryImpl @Inject constructor(
    private val lotListService: LotListService,
    private val stompClient: StompClient,
    private val internetChecker: InternetChecker,
): LotListRepository {

    override suspend fun getLots(): JsonArray {
        if (!internetChecker.isInternetAvailable()) return JsonArray()
        return lotListService.getLots("Bearer ${stompClient.token}")
    }

    override suspend fun createLot(title: String, description: String, startPrice: Float) {
        if (!internetChecker.isInternetAvailable()) return
        lotListService.createLot("Bearer ${stompClient.token}", LotApi(title, description, startPrice))
    }
}