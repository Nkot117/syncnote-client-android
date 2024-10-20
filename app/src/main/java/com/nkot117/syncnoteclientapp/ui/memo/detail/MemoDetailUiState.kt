package com.nkot117.syncnoteclientapp.ui.memo.detail

sealed interface MemoDetailUiState {
    data object Loading : MemoDetailUiState
    data object Finished: MemoDetailUiState
    data class Error(val message : String) : MemoDetailUiState
}
