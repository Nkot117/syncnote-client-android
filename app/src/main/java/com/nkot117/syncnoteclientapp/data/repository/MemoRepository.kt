package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.MemoData
import com.nkot117.syncnoteclientapp.data.model.MemoResult

interface MemoRepository {
    suspend fun getMemoList(): MemoResult
}
