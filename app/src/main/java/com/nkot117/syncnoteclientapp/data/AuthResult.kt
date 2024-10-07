package com.nkot117.syncnoteclientapp.data


sealed class AuthResult {
    data class Success(val userData: UserData) : AuthResult()
    data class Failure(val errorBody: ErrorBody) : AuthResult()
}

data class UserData(
    val name: String,
    val email: String,
    val token: String
)

data class ErrorBody(
    val message: String
)