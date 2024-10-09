package com.nkot117.syncnoteclientapp.data.model

import com.nkot117.syncnoteclientapp.network.model.LoginRequest

data class LoginData(
    val email: String,
    val password: String
) {
    fun toNetworkRequest(): LoginRequest {
        return LoginRequest(email = this.email, password = this.password)
    }
}



