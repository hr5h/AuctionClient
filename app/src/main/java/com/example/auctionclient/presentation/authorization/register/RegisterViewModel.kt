package com.example.auctionclient.presentation.authorization.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

): ViewModel() {

    private val _registerState: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun changeLogin(value: String) {
        _registerState.update { it.copy(login = value) }
    }

    fun changePassword(value: String) {
        _registerState.update { it.copy(password = value) }
    }

    fun submitRegister(callback: (Result<Unit>) -> Unit) {
        //TODO Register
        if (_registerState.value.login != "" &&
            _registerState.value.password != ""
        ) {
            _registerState.update { RegisterState() }
            callback(Result.success(Unit))
        } else {
            callback(Result.failure(Exception("Invalid login on password")))
        }
    }
}