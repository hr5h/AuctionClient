package com.example.auctionclient.data.sockets

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject

class StompClient @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var stompClient: ua.naiksoftware.stomp.StompClient? = null
    private var lifecycleDisposable: Disposable? = null
    private var topicDisposable: Disposable? = null

    private val _connectionState: MutableStateFlow<ConnectionState> = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    lateinit var token: String

    fun connect(
        url: String,
        token: String,
        onStompMessage: (StompMessage) -> Unit = {}
    ) {
        if (stompClient?.isConnected == true) return

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        val headers = listOf(
            StompHeader("Authorization", "Bearer $token")
        )

        lifecycleDisposable = stompClient?.lifecycle()
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        _connectionState.value = ConnectionState.CONNECTED
                    }
                    LifecycleEvent.Type.ERROR -> {
                        _connectionState.value = ConnectionState.ERROR(
                            lifecycleEvent.exception?.message ?: "Unknown error"
                        )
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        _connectionState.value = ConnectionState.DISCONNECTED
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        _connectionState.value = ConnectionState.ERROR("Server heartbeat failed")
                    }
                }
            }

        this.token = token
        stompClient?.connect(headers)
    }

    fun subscribe(topic: String, callback: (StompMessage) -> Unit): Disposable? {
        return stompClient?.topic(topic)?.subscribe(callback)
    }

    fun send(destination: String, payload: String) {
        stompClient?.send(destination, payload)?.subscribe()
    }

    fun disconnect() {
        topicDisposable?.dispose()
        lifecycleDisposable?.dispose()
        stompClient?.disconnect()
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    sealed class ConnectionState {
        object DISCONNECTED : ConnectionState()
        object CONNECTED : ConnectionState()
        data class ERROR(val message: String) : ConnectionState()
    }
}