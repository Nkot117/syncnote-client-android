package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.ErrorMessage
import com.nkot117.syncnoteclientapp.data.model.auth.LoginData
import com.nkot117.syncnoteclientapp.data.model.auth.RegisterData
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.model.auth.UserData
import com.nkot117.syncnoteclientapp.data.preferences.TokenManager
import com.nkot117.syncnoteclientapp.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val syncnoteServerApi: SyncnoteServerApi,
    private val moshi: Moshi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun login(loginData: LoginData): Result<UserData> {
        return try {
            val requestParams = loginData.toNetworkRequest()
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
                } ?: Result.Failure(ErrorMessage("ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
            }
        } catch (e: Exception) {
            return Result.Failure(ErrorMessage("ログインに失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
        }
    }

    override suspend fun register(registerData: RegisterData): Result<UserData?> {
        return try {
            val requestParams = registerData.toNetworkRequest()
            val response = syncnoteServerApi.register(requestParams)
            if (response.isSuccessful) {
                Result.Success(null)
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    Result.Failure(it)
                } ?: Result.Failure(ErrorMessage("ユーザー登録に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。"))
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

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorMessage? {
        return errorBody?.let {
            val adapter = moshi.adapter(ErrorMessage::class.java)
            adapter.fromJson(it.string())
        }
    }
}
