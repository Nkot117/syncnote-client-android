package com.nkot117.data.repository

import com.nkot117.data.model.Result
import com.nkot117.data.model.auth.UserData

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserData>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    fun getToken(): String
    fun clearTokens()
    suspend fun deleteAccount(): Result<Unit>
}
