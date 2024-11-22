package com.nkot117.syncnoteclientapp.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.repository.AuthRepository
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.ui.auth.register.model.RegisterFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Ideal)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _registerFormData = MutableStateFlow(RegisterFormData("", "", "", "", emptyMap()))
    val registerFormData: StateFlow<RegisterFormData> = _registerFormData.asStateFlow()

    fun onNameChanged(name: String) {
        _registerFormData.update { it.copy(name = name) }
    }

    fun onEmailChanged(email: String) {
        _registerFormData.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _registerFormData.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _registerFormData.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onSignupClicked() {
        if (!validateFormData(registerFormData.value)) {
            return
        }

        _uiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            val registerData = registerFormData.value
            val result = authRepository.register(
                name = registerData.name,
                email = registerData.email,
                password = registerData.password
            )
            if (result is Result.Success) {
                _uiState.value = RegisterUiState.Success
                Log.d("LoginViewModel", "onLoginClicked: Success")
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = RegisterUiState.Error(data.message)
                Log.d("LoginViewModel", "onLoginClicked: ${data.message}")
            }
        }
    }

    fun clearErrorState() {
        _uiState.value = RegisterUiState.Ideal
    }

    private fun validateFormData(registerFormData: RegisterFormData): Boolean {
        val name = registerFormData.name
        val email = registerFormData.email
        val password = registerFormData.password
        val confirmPassword = registerFormData.confirmPassword
        val errorMessage = mutableMapOf<String, String>()

        _registerFormData.value = registerFormData.copy(errorMessage = emptyMap())

        if (name.isEmpty()) {
            errorMessage["name"] = "名前を入力してください"
        }

        if (email.isEmpty()) {
            errorMessage["email"] = "メールアドレスを入力してください"
        }

        if (password.isEmpty()) {
            errorMessage["password"] = "パスワードを入力してください"
        }

        if (confirmPassword.isEmpty()) {
            errorMessage["confirmPassword"] = "確認パスワードを入力してください"
        }

        if (password != confirmPassword) {
            errorMessage["confirmPassword"] = "パスワードが一致しません"
        }

        _registerFormData.value = registerFormData.copy(errorMessage = errorMessage)

        return errorMessage.isEmpty()
    }
}