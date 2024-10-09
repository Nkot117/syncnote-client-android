package com.nkot117.syncnoteclientapp.data.model

import com.nkot117.syncnoteclientapp.network.model.RegisterRequest

data class RegisterData(
    val name: String,
    val email: String,
    val password: String
){
    fun toNetworkRequest(): RegisterRequest {
        return RegisterRequest(name = this.name, email = this.email, password = this.password)
    }
}
