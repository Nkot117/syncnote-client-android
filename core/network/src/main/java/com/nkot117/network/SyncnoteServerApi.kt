package com.nkot117.network

import com.nkot117.network.model.login.LoginRequest
import com.nkot117.network.model.login.LoginResponse
import com.nkot117.network.model.login.RefreshTokenRequest
import com.nkot117.network.model.login.RefreshTokenResponse
import com.nkot117.network.model.memo.MemoDetailResponse
import com.nkot117.network.model.memo.MemoListResponse
import com.nkot117.network.model.memo.MemoUpdateRequest
import com.nkot117.network.model.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface SyncnoteServerApi {
    @POST("api/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Void>

    @POST("api/user/refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): Response<RefreshTokenResponse>

    @DELETE("api/user/delete")
    suspend fun deleteUser(@Header("Authorization") token: String): Response<Void>

    @GET("api/memo/list")
    suspend fun getMemoList(@Header("Authorization") token: String): Response<MemoListResponse>

    @GET("api/memo/{id}")
    suspend fun getMemoDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<MemoDetailResponse>

    @PATCH("api/memo/{id}")
    suspend fun updateMemo(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body memoContent: MemoUpdateRequest
    ): Response<MemoDetailResponse>

    @POST("api/memo/create")
    suspend fun createMemo(
        @Header("Authorization") token: String,
        @Body memoContent: MemoUpdateRequest
    ): Response<MemoDetailResponse>

    @DELETE("api/memo/{id}")
    suspend fun deleteMemo(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<Void>
}
