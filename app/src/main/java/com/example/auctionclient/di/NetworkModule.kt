package com.example.auctionclient.di

import com.example.auctionclient.data.services.BidService
import com.example.auctionclient.data.services.LoginService
import com.example.auctionclient.data.services.LotListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun provideTotalService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    fun provideLotListService(retrofit: Retrofit): LotListService {
        return retrofit.create(LotListService::class.java)
    }

    @Provides
    fun provideBidService(retrofit: Retrofit): BidService {
        return retrofit.create(BidService::class.java)
    }

    val BASE_URL = "http://10.0.2.2:8080/api/"
}
