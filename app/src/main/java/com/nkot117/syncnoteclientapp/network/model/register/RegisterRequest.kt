package com.nkot117.syncnoteclientapp.network.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
