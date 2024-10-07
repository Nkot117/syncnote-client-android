package com.nkot117.syncnoteclientapp.network

import com.nkot117.syncnoteclientapp.network.model.LoginRequest
import com.nkot117.syncnoteclientapp.network.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SyncnoteServerApi {
    @POST("api/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}