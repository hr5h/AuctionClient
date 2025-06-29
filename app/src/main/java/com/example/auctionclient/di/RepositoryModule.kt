package com.example.auctionclient.di

import com.example.auctionclient.data.repo.LoginRepository
import com.example.auctionclient.data.repo.LoginRepositoryImpl
import com.example.auctionclient.data.repo.LotDetailRepository
import com.example.auctionclient.data.repo.LotDetailRepositoryImpl
import com.example.auctionclient.data.repo.LotListRepository
import com.example.auctionclient.data.repo.LotListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl,
    ): LoginRepository

    @Binds
    abstract fun bindLotListRepository(
        impl: LotListRepositoryImpl,
    ): LotListRepository

    @Binds
    abstract fun bindLotDetailRepository(
        impl: LotDetailRepositoryImpl,
    ): LotDetailRepository
}