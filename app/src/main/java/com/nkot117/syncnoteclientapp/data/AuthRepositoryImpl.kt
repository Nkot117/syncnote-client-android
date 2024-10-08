package com.nkot117.syncnoteclientapp.data

import com.nkot117.syncnoteclientapp.network.SyncnoteServerApi
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val syncnoteServerApi: SyncnoteServerApi,
    private val moshi: Moshi
) : AuthRepository {
    override suspend fun login(loginData: LoginData): AuthResult {
        return try {
            val requestParams = loginData.toNetworkRequest()
            val response = syncnoteServerApi.login(requestParams)
            if (response.isSuccessful) {
                response.body()?.let {
                    AuthResult.Success(UserData(it.userInfo.name, it.userInfo.email, it.token))
                } ?: AuthResult.Failure(ErrorMessage("Unknown error"))
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                errorResponse?.let {
                    AuthResult.Failure(it)
                } ?: AuthResult.Failure(ErrorMessage("Unknown error"))
            }
        } catch (e: Exception) {
            return AuthResult.Failure(ErrorMessage("Unknown error"))
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorMessage? {
        return errorBody?.let {
            val adapter = moshi.adapter(ErrorMessage::class.java)
            adapter.fromJson(it.string())
        }
    }
}
