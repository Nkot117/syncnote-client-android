package com.nkot117.syncnoteclientapp.data.repository

import com.nkot117.syncnoteclientapp.data.model.auth.LoginData
import com.nkot117.syncnoteclientapp.data.model.auth.RegisterData
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.model.auth.UserData

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserData>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    fun getToken(): String
    fun clearTokens()
    suspend fun deleteAccount(): Result<Unit>
}
