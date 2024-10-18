package com.nkot117.syncnoteclientapp.data.model.auth

import com.nkot117.syncnoteclientapp.network.model.register.RegisterRequest

data class RegisterData(
    val name: String,
    val email: String,
    val password: String
){
    fun toNetworkRequest(): RegisterRequest {
        return RegisterRequest(name = this.name, email = this.email, password = this.password)
    }
}
