package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.auth.LoginData
import com.nkot117.syncnoteclientapp.data.model.auth.RegisterData
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.model.auth.UserData

interface AuthRepository {
    suspend fun login(loginData: LoginData): Result<UserData>

    suspend fun register(registerData: RegisterData): Result<UserData?>

    fun getToken(): String

    fun clearTokens()

    suspend fun deleteAccount(): Result<Unit>
}
