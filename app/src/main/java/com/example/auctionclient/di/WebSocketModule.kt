package com.example.auctionclient.di

import android.content.Context
import com.example.auctionclient.data.sockets.StompClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideStompClient(@ApplicationContext context: Context): StompClient {
        return StompClient(context)
    }
}