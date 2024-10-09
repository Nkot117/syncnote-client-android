package com.nkot117.syncnoteclientapp.network.model

import com.squareup.moshi.Json

data class RegisterResponse(
    @Json(name = "userWithoutPassword")
    val userWithoutPassword: UserInfo,
)


