package com.example.auctionclient.data.repo

import com.example.auctionclient.data.services.BidService
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.data.utils.InternetChecker
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import kotlinx.serialization.json.Json
import org.json.JSONObject
import javax.inject.Inject

interface LotDetailRepository {

    suspend fun getBids(lotId: Long): List<Bid>
    suspend fun createBid(lotId: Long, amount: Long)
    suspend fun subscribeLot(lotId: Long, callback: (Lot) -> Unit)
}

class LotDetailRepositoryImpl @Inject constructor(
    private val bidService: BidService,
    private val stompClient: StompClient,
    private val internetChecker: InternetChecker,
): LotDetailRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun getBids(lotId: Long): List<Bid> {
        if (!internetChecker.isInternetAvailable()) return emptyList()
        return bidService.getBids(lotId, "Bearer ${stompClient.token}")
    }

    override suspend fun createBid(lotId: Long, amount: Long) {
        if (!internetChecker.isInternetAvailable()) return
        val destination = "/app/lot/$lotId/bid"
        val payload = JSONObject().apply {
            put("amount", amount)
        }.toString()

        stompClient.send(destination, payload)
    }

    override suspend fun subscribeLot(lotId: Long, callback: (Lot) -> Unit) {
        stompClient.subscribe("/topic/lot/$lotId") { message ->
            val payloadString = message.payload as? String ?: ""
            val lot = json.decodeFromString<Lot>(payloadString)

            callback(lot)
        }
    }
}