package com.nkot117.syncnoteclientapp.data.model

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val errorMessage: ErrorMessage) : Result<Nothing>()
}

data class ErrorMessage(
    val message: String,
    val reason: String? = null
)