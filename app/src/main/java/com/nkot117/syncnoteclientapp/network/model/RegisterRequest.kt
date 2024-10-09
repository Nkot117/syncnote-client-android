package com.nkot117.syncnoteclientapp.network.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
