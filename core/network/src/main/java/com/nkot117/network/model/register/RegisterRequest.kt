package com.nkot117.network.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
