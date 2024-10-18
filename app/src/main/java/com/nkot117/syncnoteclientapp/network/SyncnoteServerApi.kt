package com.nkot117.syncnoteclientapp.network

import com.nkot117.syncnoteclientapp.network.model.login.LoginRequest
import com.nkot117.syncnoteclientapp.network.model.login.LoginResponse
import com.nkot117.syncnoteclientapp.network.model.memo.MemoListResponse
import com.nkot117.syncnoteclientapp.network.model.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SyncnoteServerApi {
    @POST("api/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Void>

    @GET("api/memo/list")
    suspend fun getMemoList(@Header("Authorization") token: String): Response<MemoListResponse>
}
