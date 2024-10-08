package com.nkot117.syncnoteclientapp.network

import com.nkot117.syncnoteclientapp.network.model.LoginRequest
import com.nkot117.syncnoteclientapp.network.model.LoginResponse
import com.nkot117.syncnoteclientapp.network.model.MemoListResponse
import com.nkot117.syncnoteclientapp.network.model.RegisterRequest
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

    @GET("api/list")
    suspend fun getMemoList(@Header("Authorization") token: String): Response<MemoListResponse>
}
