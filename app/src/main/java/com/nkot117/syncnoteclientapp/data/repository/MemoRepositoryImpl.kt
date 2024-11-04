package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.ErrorMessage
import com.nkot117.syncnoteclientapp.data.model.MemoData
import com.nkot117.syncnoteclientapp.data.model.Result
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
    override suspend fun getMemoList(): Result<List<MemoData>> {
        return try {
            val token = tokenManager.getAccessToken()
                ?: return Result.Failure(ErrorMessage("Token not found"))

            val response = syncnoteServerApi.getMemoList("Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let {
                    val memoList = it.memoList.map { memoInfo ->
                        MemoData(
                            id = memoInfo.id,
                            title = memoInfo.title,
                            content = memoInfo.content
                        )
                    }
                    Result.Success(memoList)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    if (it.message === "トークンの有効期限が切れました") {
                        // トークンの有効期限切れエラーの場合、トークンをリフレッシュして再度リクエストを送る
                        val refreshResult = refreshAccessToken()
                        return if (refreshResult is Result.Success) {
                            getMemoList()
                        } else {
                            Result.Failure(ErrorMessage("Unknown error"))
                        }
                    } else {
                        return Result.Failure(it)
                    }
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }
    }

    override suspend fun getMemoDetail(id: String): Result<MemoData> {
        return try {
            val token = tokenManager.getAccessToken()
                ?: return Result.Failure(ErrorMessage("Token not found"))

            val response = syncnoteServerApi.getMemoDetail(id, "Bearer $token")

            return if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(
                        MemoData(
                            id = it.memo.id,
                            title = it.memo.title,
                            content = it.memo.content
                        )
                    )
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }
    }

    override suspend fun updateMemo(id: String, title: String, content: String): Result<MemoData> {
        return try {
            val token = tokenManager.getAccessToken()
                ?: return Result.Failure(ErrorMessage("Token not found"))

            val updateMemoData = MemoData(
                id = id,
                title = title,
                content = content
            )

            val response = syncnoteServerApi.updateMemo(
                updateMemoData.id,
                "Bearer $token",
                updateMemoData.toNetworkRequest()
            )

            return if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(
                        MemoData(
                            id = it.memo.id,
                            title = it.memo.title,
                            content = it.memo.content
                        )
                    )
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }

        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }

    }

    override suspend fun createMemo(title: String, content: String): Result<MemoData> {
        return try {
            val token = tokenManager.getAccessToken()
                ?: return Result.Failure(ErrorMessage("Token not found"))
            val newMemoData = MemoData(
                id = "",
                title = title,
                content = content
            )
            val response = syncnoteServerApi.createMemo(
                "Bearer $token",
                newMemoData.toNetworkRequest()
            )

            return if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(
                        MemoData(
                            id = it.memo.id,
                            title = it.memo.title,
                            content = it.memo.content
                        )
                    )
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }

        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }
    }

    override suspend fun deleteMemo(id: String): Result<Unit> {
        return try {
            val token =
                tokenManager.getAccessToken()
                    ?: return Result.Failure(ErrorMessage("Token not found"))
            val response = syncnoteServerApi.deleteMemo(id, "Bearer $token")
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }
    }

    private suspend fun refreshAccessToken(): Result<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return Result.Failure(ErrorMessage("Refresh token not found"))

            val response = syncnoteServerApi.refreshToken(refreshToken)

            return if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveAccessToken(it)
                    Result.Success(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Failure(ErrorMessage("Unknown error"))
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorMessage? {
        return errorBody?.let {
            val adapter = moshi.adapter(ErrorMessage::class.java)
            adapter.fromJson(it.string())
        }
    }
}
