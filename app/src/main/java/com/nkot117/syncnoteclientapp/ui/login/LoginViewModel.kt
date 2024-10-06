package com.nkot117.syncnoteclientapp.ui.login

import androidx.lifecycle.ViewModel
import com.nkot117.syncnoteclientapp.ui.model.LoginFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
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
}