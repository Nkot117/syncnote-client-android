package com.nkot117.syncnoteclientapp.ui


sealed interface MemoListUiState {
    data object Loading : MemoListUiState
    data class Success(val memoList : List<MemoData>) : MemoListUiState
    data class Error(val message: String) : MemoListUiState
}
