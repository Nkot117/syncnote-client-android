package com.nkot117.syncnoteclientapp.data.model.memo

import com.nkot117.syncnoteclientapp.network.model.memo.MemoUpdateRequest

data class MemoData(
    val id: String,
    val title: String,
    val content: String,
) {
    fun toNetworkRequest(): MemoUpdateRequest {
        return MemoUpdateRequest(title = this.title, content = this.content)
    }
}
