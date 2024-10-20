package com.nkot117.syncnoteclientapp.network.model.memo

import com.squareup.moshi.Json

data class MemoInfo(
    @Json(name = "_id")
    val id: String,
    val title: String,
    val content: String,
)
