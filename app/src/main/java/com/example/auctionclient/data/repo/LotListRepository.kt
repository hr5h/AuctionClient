package com.example.auctionclient.data.repo

import android.util.Base64
import android.util.Log
import com.example.auctionclient.data.entities.LotApi
import com.example.auctionclient.data.services.LotListService
import com.example.auctionclient.data.sockets.StompClient
import com.example.auctionclient.data.utils.InternetChecker
import com.example.auctionclient.domain.Lot
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

interface LotListRepository {

    suspend fun getLots(): List<Lot>
    suspend fun getWinningLots(): List<Lot>
    suspend fun createLot(title: String, description: String, startPrice: Float)
}

class LotListRepositoryImpl @Inject constructor(
    private val lotListService: LotListService,
    private val stompClient: StompClient,
    private val internetChecker: InternetChecker,
): LotListRepository {

    override suspend fun getLots(): List<Lot> {
        if (!internetChecker.isInternetAvailable()) return emptyList()
        return lotListService.getLots("Bearer ${stompClient.token}").map { lot ->
            lot.copy(endTime = extractTime(lot.endTime ?: ""))
        }
    }

    override suspend fun getWinningLots(): List<Lot> {
        if (!internetChecker.isInternetAvailable()) return emptyList()
        return lotListService.getWinningLots(getUserName(),"Bearer ${stompClient.token}").map { lot ->
            lot.copy(endTime = extractTime(lot.endTime ?: ""))
        }
    }

    override suspend fun createLot(title: String, description: String, startPrice: Float) {
        if (!internetChecker.isInternetAvailable()) return
        lotListService.createLot("Bearer ${stompClient.token}", LotApi(title, description, startPrice))
    }

    private fun getUserName(): String {
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