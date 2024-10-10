package com.nkot117.syncnoteclientapp.data.repository

import android.util.Log
import com.nkot117.syncnoteclientapp.data.model.AuthResult
import com.nkot117.syncnoteclientapp.data.model.ErrorMessage
import com.nkot117.syncnoteclientapp.data.model.LoginData
import com.nkot117.syncnoteclientapp.data.model.RegisterData
import com.nkot117.syncnoteclientapp.data.model.UserData
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
    override suspend fun login(loginData: LoginData): AuthResult {
        return try {
            val requestParams = loginData.toNetworkRequest()
            val response = syncnoteServerApi.login(requestParams)
            if (response.isSuccessful) {
                response.body()?.let {
                    tokenManager.saveToken(it.token)
                    AuthResult.Success(UserData(name = it.userInfo.name, email = it.userInfo.email))
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

    override suspend fun register(registerData: RegisterData): AuthResult {
        return try {
            val requestParams = registerData.toNetworkRequest()
            val response = syncnoteServerApi.register(requestParams)
            if (response.isSuccessful) {
                AuthResult.Success(null)
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
