package com.nkot117.syncnoteclientapp.data

import com.nkot117.syncnoteclientapp.data.model.AuthResult
import com.nkot117.syncnoteclientapp.data.model.LoginData
import com.nkot117.syncnoteclientapp.data.model.RegisterData

interface AuthRepository {
    suspend fun login(loginData: LoginData): AuthResult

    suspend fun register(registerData: RegisterData): AuthResult
}