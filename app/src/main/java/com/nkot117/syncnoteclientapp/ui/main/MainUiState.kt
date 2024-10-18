package com.nkot117.syncnoteclientapp.ui.main

sealed interface MainUiState {
    data object Loading : MainUiState
    data object Finished : MainUiState
}