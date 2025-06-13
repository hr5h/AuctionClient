package com.example.auctionclient.di

import com.example.auctionclient.data.repo.LoginRepository
import com.example.auctionclient.data.repo.LoginRepositoryImpl
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
}