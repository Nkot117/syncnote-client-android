package com.nkot117.data.model.memo

import com.nkot117.network.model.memo.MemoUpdateRequest

data class MemoData(
    val id: String,
    val title: String,
    val content: String,
) {
    fun toNetworkRequest(): MemoUpdateRequest {
        return MemoUpdateRequest(title = this.title, content = this.content)
    }
}
