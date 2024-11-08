package com.nkot117.syncnoteclientapp.ui.memo.list

import com.nkot117.syncnoteclientapp.ui.memo.MemoData


sealed interface MemoListUiState {
    data object Loading : MemoListUiState
    data class Success(val memoList : List<MemoData>, val isRefreshing: Boolean) : MemoListUiState
    data class Error(val message: String) : MemoListUiState
}
