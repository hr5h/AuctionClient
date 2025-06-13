package com.example.auctionclient.data.repo

import com.example.auctionclient.data.entities.LoginRequest
import com.example.auctionclient.data.services.LoginService
import javax.inject.Inject

interface LoginRepository {

    suspend fun login(login: String, password: String): String
}

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
): LoginRepository {

    override suspend fun login(login: String, password: String): String {
        return loginService.login(LoginRequest(login, password)).token
    }
}