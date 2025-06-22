package com.example.auctionclient.data.repo

import com.example.auctionclient.data.services.BidService
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.data.utils.InternetChecker
import com.example.auctionclient.domain.Bid
import org.json.JSONObject
import javax.inject.Inject

interface LotDetailRepository {

    suspend fun getBids(lotId: Long): List<Bid>
    suspend fun createBid(lotId: Long, amount: Long)
}

class LotDetailRepositoryImpl @Inject constructor(
    private val bidService: BidService,
    private val stompClient: StompClient,
    private val internetChecker: InternetChecker,
): LotDetailRepository {

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
}