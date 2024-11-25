package com.nkot117.data.model.auth

import com.nkot117.network.model.login.RefreshTokenRequest

data class RefreshTokenData(
    val refreshToken: String
) {
    fun toNetworkRequest(): RefreshTokenRequest {
        return RefreshTokenRequest(refreshToken)
    }
}