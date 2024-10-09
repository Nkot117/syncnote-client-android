package com.nkot117.syncnoteclientapp.ui.auth.register

sealed interface RegisterUiState {
    data object Ideal : RegisterUiState
    data object Loading : RegisterUiState
    data object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

