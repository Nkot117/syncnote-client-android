package com.nkot117.syncnoteclientapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.AuthRepository
import com.nkot117.syncnoteclientapp.data.AuthResult
import com.nkot117.syncnoteclientapp.data.LoginData
import com.nkot117.syncnoteclientapp.ui.model.LoginFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginFormData = MutableStateFlow(LoginFormData("", ""))
    val loginFormData : StateFlow<LoginFormData> = _loginFormData.asStateFlow()

    fun onEmailChanged(email: String) {
        _loginFormData.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _loginFormData.update { it.copy(password = password) }
    }

    fun onLoginClicked() {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val loginData = loginFormData.value
            val result = authRepository.login(LoginData(email = loginData.email, password = loginData.password))
            if(result is AuthResult.Success) {
                val data = result.userData
                Log.d("LoginViewModel", "onLoginClicked: $data")
            } else {
                val data = (result as AuthResult.Failure).errorBody
                Log.d("LoginViewModel", "onLoginClicked: ${data.message}")
            }
        }
    }
}