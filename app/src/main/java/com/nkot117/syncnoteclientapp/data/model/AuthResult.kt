package com.nkot117.syncnoteclientapp.data.model


sealed class AuthResult {
    data class Success(val userData: UserData?) : AuthResult()
    data class Failure(val errorMessage: ErrorMessage) : AuthResult()
}


data class ErrorMessage(
    val message: String
)