package com.nkot117.syncnoteclientapp.data

import retrofit2.Response

interface AuthRepository {
    suspend fun login(loginData: LoginData): AuthResult
}