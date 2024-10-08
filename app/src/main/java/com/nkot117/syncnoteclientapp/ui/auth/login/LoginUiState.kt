package com.nkot117.syncnoteclientapp.ui.auth.login

sealed interface LoginUiState {
    data object Ideal : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

