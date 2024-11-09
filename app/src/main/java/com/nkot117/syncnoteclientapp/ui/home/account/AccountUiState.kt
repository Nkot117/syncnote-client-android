package com.nkot117.syncnoteclientapp.ui.home.account

sealed interface AccountUiState {
    data object Ideal: AccountUiState
    data object Loading: AccountUiState
    data object Success: AccountUiState
    data object Error: AccountUiState
}