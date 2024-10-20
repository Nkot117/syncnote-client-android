package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.MemoData
import com.nkot117.syncnoteclientapp.data.model.Result

interface MemoRepository {
    suspend fun getMemoList(): Result<List<MemoData>>
    suspend fun getMemoDetail(id: String): Result<MemoData>
}
