package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.memo.MemoData
import com.nkot117.syncnoteclientapp.data.model.Result

interface MemoRepository {
    suspend fun getMemoList(): Result<List<MemoData>>
}
