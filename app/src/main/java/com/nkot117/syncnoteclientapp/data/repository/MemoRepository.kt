package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.MemoData
import com.nkot117.syncnoteclientapp.data.model.Result

interface MemoRepository {
    suspend fun getMemoList(): Result<List<MemoData>>
    suspend fun getMemoDetail(id: String): Result<MemoData>
    suspend fun updateMemo(id: String, title: String, content: String): Result<MemoData>
    suspend fun createMemo(title: String, content: String): Result<MemoData>
    suspend fun deleteMemo(id: String): Result<Unit>
}
