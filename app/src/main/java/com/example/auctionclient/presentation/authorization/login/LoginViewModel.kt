package com.example.auctionclient.presentation.authorization.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.data.repo.LoginRepository
import com.example.auctionclient.data.sockets.StompClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val URL_SERVER = "http://10.0.2.2:8080/ws-auction"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val stompClient: StompClient
) : ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun changeLogin(value: String) {
        _loginState.update { it.copy(login = value) }
    }

    fun changePassword(value: String) {
        _loginState.update { it.copy(password = value) }
    }

    fun submitLogin(type: String, callback: (Result<Unit>) -> Unit) {
        if (_loginState.value.login != "" &&
            _loginState.value.password != ""
        ) {
            viewModelScope.launch{
                val tokenDef = async {
                    if(type == "login") {
                        loginRepository.login(_loginState.value.login, _loginState.value.password)
                    } else {
                        loginRepository.register(_loginState.value.login, _loginState.value.password)
                    }
                }

                val token = tokenDef.await()
                //println(token)
                stompClient.connect(URL_SERVER, token)
                _loginState.update { LoginState() }
                callback(Result.success(Unit))
            }
        } else {
            callback(Result.failure(Exception("Invalid login or password")))
        }
    }
}