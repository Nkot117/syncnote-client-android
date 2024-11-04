package com.nkot117.syncnoteclientapp.network.model.login

import com.squareup.moshi.Json

data class LoginResponse (
    @Json(name = "userWithoutPassword")
    val userInfo: UserInfo,
    val accessToken: String,
    val refreshToken: String
)