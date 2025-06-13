package com.example.auctionclient.presentation.authorization.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctionclient.data.repo.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
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
        if (_loginState.value.login != "" &&
            _loginState.value.password != ""
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val token = async { loginRepository.login(_loginState.value.login, _loginState.value.password) }

                println(token.await())
            }
            _loginState.update { LoginState() }
            callback(Result.success(Unit))
        } else {
            callback(Result.failure(Exception("Invalid login or password")))
        }
    }
}