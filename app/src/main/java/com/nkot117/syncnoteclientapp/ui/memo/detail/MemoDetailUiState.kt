package com.nkot117.syncnoteclientapp.ui.memo.detail

import com.nkot117.syncnoteclientapp.ui.memo.MemoData

sealed interface MemoDetailUiState {
    data object Loading : MemoDetailUiState
    data class Finished(val memo : MemoData) : MemoDetailUiState
    data class Error(val message : String) : MemoDetailUiState
}
