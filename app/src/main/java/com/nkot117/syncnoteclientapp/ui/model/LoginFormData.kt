package com.nkot117.syncnoteclientapp.ui.model

data class LoginFormData(
    val email: String,
    val password: String,
    val errorMessage: Map<String, String>
)
