package com.example.auctionclient.presentation.authorization.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Callback
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun changeLogin(value: String) {
        _loginState.update { it.copy(login = value) }
    }

    fun changePassword(value: String) {
        _loginState.update { it.copy(password = value) }
    }

    fun submitLogin(callback: (Result<Unit>) -> Unit) {
        //TODO Login
        if (_loginState.value.login != "" &&
            _loginState.value.password != ""
        ) {
            _loginState.update { LoginState() }
            callback(Result.success(Unit))
        } else {
            callback(Result.failure(Exception("Invalid login on password")))
        }
    }
}