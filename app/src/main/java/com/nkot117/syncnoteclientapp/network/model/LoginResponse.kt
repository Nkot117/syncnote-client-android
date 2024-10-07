package com.nkot117.syncnoteclientapp.network.model

import com.squareup.moshi.Json

data class LoginResponse (
    @Json(name = "userWithoutPassword")
    val userInfo: UserInfo,
    val token: String
)

data class UserInfo (
    val name: String,
    val email: String,
)