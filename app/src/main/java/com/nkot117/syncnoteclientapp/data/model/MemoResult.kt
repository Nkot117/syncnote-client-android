package com.nkot117.syncnoteclientapp.data.model

sealed class MemoResult {
    data class Success(val memoList: List<MemoData>) : MemoResult()
    data class Failure(val errorMessage: ErrorMessage) : MemoResult()
}
