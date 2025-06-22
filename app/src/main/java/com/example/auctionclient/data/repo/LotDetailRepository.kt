package com.example.auctionclient.data.repo

import android.util.Base64
import android.util.Log
import com.example.auctionclient.data.services.BidService
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.data.utils.InternetChecker
import com.example.auctionclient.domain.Bid
import com.example.auctionclient.domain.Lot
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

interface LotDetailRepository {

    suspend fun getBids(lotId: Long): List<Bid>
    suspend fun createBid(lotId: Long, amount: Long)
    suspend fun subscribeLot(lotId: Long, callback: (Lot) -> Unit)
    suspend fun finalizeLot(lotId: Long)
    fun getUserName(): String
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
        return bidService.getBids(lotId, "Bearer ${stompClient.token}").map { bid ->
            bid.copy(timestamp = extractTime(bid.timestamp))
        }
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

            callback(lot.copy(endTime = extractTime(lot.endTime ?: "")))
        }
    }

    override suspend fun finalizeLot(lotId: Long) {
        bidService.finalizeLot(lotId, "Bearer ${stompClient.token}")
    }

    override fun getUserName(): String {
        return getCurrentUserId(stompClient.token) ?: ""
    }

    private fun getCurrentUserId(jwtToken: String?): String? {
        if (jwtToken.isNullOrEmpty()) return null

        return try {
            val payloadBase64 = jwtToken.split('.')[1]
                .replace("-", "+")
                .replace("_", "/")

            val payloadJson = String(Base64.decode(payloadBase64, Base64.DEFAULT))
            val payload = JSONObject(payloadJson)

            payload.optString("userId").takeIf { it.isNotEmpty() }
                ?: payload.optString("sub").takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            Log.e("JWT", "Failed to parse JWT", e)
            null
        }
    }

    private fun extractTime(isoString: String): String {
        if(isoString == "") return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(isoString)

        date.time += 3 * 60 * 60 * 1000

        val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return date?.let { outputFormat.format(it) } ?: ""
    }
}