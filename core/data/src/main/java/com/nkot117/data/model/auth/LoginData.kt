package com.nkot117.data.model.auth

import com.nkot117.network.model.login.LoginRequest

data class LoginData(
    val email: String,
    val password: String
) {
    fun toNetworkRequest(): LoginRequest {
        return LoginRequest(email = this.email, password = this.password)
    }
}
