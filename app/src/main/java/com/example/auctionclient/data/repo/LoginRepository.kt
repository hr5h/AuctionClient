package com.example.auctionclient.data.repo

import com.example.auctionclient.data.entities.LoginRequest
import com.example.auctionclient.data.services.LoginService
import com.example.auctionclient.data.utils.InternetChecker
import javax.inject.Inject

interface LoginRepository {

    suspend fun login(login: String, password: String): String
    suspend fun register(login: String, password: String): String
}

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val internetChecker: InternetChecker,
): LoginRepository {

    override suspend fun login(login: String, password: String): String {
        if(!internetChecker.isInternetAvailable()) return ""
        return loginService.login(LoginRequest(login, password)).token
    }

    override suspend fun register(login: String, password: String): String {
        if(!internetChecker.isInternetAvailable()) return ""
        return loginService.register(LoginRequest(login, password)).token
    }
}