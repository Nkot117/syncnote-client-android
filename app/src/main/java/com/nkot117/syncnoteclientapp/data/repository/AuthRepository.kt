package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.LoginData
import com.nkot117.syncnoteclientapp.data.model.RegisterData
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.model.UserData

interface AuthRepository {
    suspend fun login(loginData: LoginData): Result<UserData>

    suspend fun register(registerData: RegisterData): Result<UserData?>

    fun getToken(): String
}
