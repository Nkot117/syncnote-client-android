package com.nkot117.syncnoteclientapp.ui.memo


sealed interface MemoListUiState {
    data object Loading : MemoListUiState
    data class Success(val memoList : List<MemoData>, val isRefreshing: Boolean) : MemoListUiState
    data class Error(val message: String) : MemoListUiState
}
