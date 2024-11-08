package com.nkot117.syncnoteclientapp.data.model.memo

import com.nkot117.syncnoteclientapp.network.model.memo.MemoContent

data class MemoData(
    val id: String,
    val title: String,
    val content: String,
) {
    fun toNetworkRequest(): MemoContent {
        return MemoContent(title = this.title, content = this.content)
    }
}
