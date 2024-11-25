package com.nkot117.network.model.register

import com.nkot117.network.model.login.UserInfo
import com.squareup.moshi.Json

data class RegisterResponse(
    @Json(name = "userWithoutPassword")
    val userWithoutPassword: UserInfo,
)


