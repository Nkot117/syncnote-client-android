package com.nkot117.syncnoteclientapp.data.model.auth

import com.nkot117.syncnoteclientapp.network.model.login.RefreshTokenRequest

data class RefreshTokenData(
    val refreshToken: String
) {
    fun toNetworkRequest(): RefreshTokenRequest {
        return RefreshTokenRequest(refreshToken)
    }
}