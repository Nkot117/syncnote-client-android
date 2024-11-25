package com.nkot117.network.model.memo

data class MemoListResponse(
    val memoList: List<MemoInfo>
)

data class MemoInfo(
    val id: String,
    val title: String,
    val content: String,
)