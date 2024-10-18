package com.nkot117.syncnoteclientapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.repository.AuthRepository
import com.nkot117.syncnoteclientapp.data.model.auth.LoginData
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.ui.auth.login.model.LoginFormData
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
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Ideal)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginFormData = MutableStateFlow(LoginFormData("", "", emptyMap()))
    val loginFormData : StateFlow<LoginFormData> = _loginFormData.asStateFlow()

    fun onEmailChanged(email: String) {
        _loginFormData.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _loginFormData.update { it.copy(password = password) }
    }

    fun onLoginClicked() {
        if(!validateFormData(loginFormData.value)) {
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val loginData = loginFormData.value
            val result = authRepository.login(LoginData(email = loginData.email, password = loginData.password))
            if(result is Result.Success) {
                val data = result.data
                _uiState.value = LoginUiState.Success
                Log.d("LoginViewModel", "onLoginClicked: $data")
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = LoginUiState.Error(data.message)
                Log.d("LoginViewModel", "onLoginClicked: ${data.message}")
            }
        }
    }

    private fun validateFormData(loginData: LoginFormData): Boolean {
        val email = loginData.email
        val password = loginData.password
        val errorMessage = mutableMapOf<String, String>()

        _loginFormData.value = loginData.copy(errorMessage = emptyMap())

        if(email.isEmpty()) {
            errorMessage["email"] = "メールアドレスを入力してください"
        }

        if(password.isEmpty()) {
            errorMessage["password"] = "パスワードを入力してください"
        }

        _loginFormData.value = loginFormData.value.copy(errorMessage = errorMessage)

        return errorMessage.isEmpty()
    }
}