package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.ErrorMessage
import com.nkot117.syncnoteclientapp.data.model.MemoData
import com.nkot117.syncnoteclientapp.data.model.MemoResult
import com.nkot117.syncnoteclientapp.data.preferences.TokenManager
import com.nkot117.syncnoteclientapp.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val syncnoteServerApi: SyncnoteServerApi,
    private val moshi: Moshi,
    private val tokenManager: TokenManager
) : MemoRepository {
    override suspend fun getMemoList()  : MemoResult {
        return try {
            val token = tokenManager.getToken() ?: return MemoResult.Failure(ErrorMessage("Token not found"))

            val response = syncnoteServerApi.getMemoList(token)

            if (response.isSuccessful) {
                response.body()?.let {
                    val memoList = it.memos.map { memoInfo ->
                        MemoData(
                            title = memoInfo.title,
                            content = memoInfo.content
                        )
                    }
                    MemoResult.Success(memoList)
                } ?: MemoResult.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    MemoResult.Failure(it)
                } ?: MemoResult.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            MemoResult.Failure(ErrorMessage("Unknown error"))
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorMessage? {
        return errorBody?.let {
            val adapter = moshi.adapter(ErrorMessage::class.java)
            adapter.fromJson(it.string())
        }
    }
}
