package com.nkot117.syncnoteclientapp.ui.auth.register.model

data class RegisterFormData(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val errorMessage: Map<String, String>
)
