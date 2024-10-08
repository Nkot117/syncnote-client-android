package com.nkot117.syncnoteclientapp.ui.auth.login.model

data class LoginFormData(
    val email: String,
    val password: String,
    val errorMessage: Map<String, String>
)
