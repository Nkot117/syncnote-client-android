package com.nkot117.data.repository

import com.nkot117.data.model.ErrorMessage
import com.nkot117.data.model.auth.LoginData
import com.nkot117.data.model.auth.RegisterData
import com.nkot117.data.model.Result
import com.nkot117.data.model.auth.UserData
import com.nkot117.data.preferences.TokenManager
import com.nkot117.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val syncnoteServerApi: SyncnoteServerApi,
    private val moshi: Moshi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<UserData> {
        return try {
            val requestParams = LoginData(email, password).toNetworkRequest()
            val response = syncnoteServerApi.login(requestParams)
            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveAccessToken(it.accessToken)
                    tokenManager.saveRefreshToken(it.refreshToken)
                    Result.Success(UserData(name = it.userInfo.name, email = it.userInfo.email))
                } ?: Result.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                }
                    ?: Result.Failure(ErrorMessage("ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
            }
        } catch (e: Exception) {
            return Result.Failure(ErrorMessage("ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val requestParams = RegisterData(name, email, password).toNetworkRequest()
            val response = syncnoteServerApi.register(requestParams)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                }
                    ?: Result.Failure(ErrorMessage("ユーザー登録に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
            }
        } catch (e: Exception) {
            return Result.Failure(ErrorMessage("ユーザー登録に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
        }
    }

    override fun getToken(): String {
        return tokenManager.getAccessToken() ?: ""
    }

    override fun clearTokens() {
        tokenManager.clearTokens()
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = syncnoteServerApi.deleteUser("Bearer ${tokenManager.getAccessToken()}")
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                }
                    ?: Result.Failure(ErrorMessage("アカウントの削除に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
            }
        } catch (e: Exception) {
            return Result.Failure(ErrorMessage("アカウントの削除に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorMessage? {
        return errorBody?.let {
            val adapter = moshi.adapter(ErrorMessage::class.java)
            adapter.fromJson(it.string())
        }
    }
}
