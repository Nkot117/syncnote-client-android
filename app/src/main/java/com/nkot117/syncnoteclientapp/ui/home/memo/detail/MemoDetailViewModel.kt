package com.nkot117.syncnoteclientapp.ui.home.memo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.data.model.Result
import com.nkot117.data.repository.MemoRepository
import com.nkot117.syncnoteclientapp.ui.home.memo.MemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val memoRepository: com.nkot117.data.repository.MemoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MemoDetailUiState>(MemoDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _memoData = MutableStateFlow(MemoData("", "", ""))
    val memoData = _memoData.asStateFlow()

    private var initMemoData = MemoData("", "", "")

    fun loadMemo(id: String?) {
        _uiState.value = MemoDetailUiState.Loading

        if (id == null) {
            viewModelScope.launch {
                val result = memoRepository.createMemo("", "")
                if (result is com.nkot117.data.model.Result.Success) {
                    val memoData = MemoData(
                        id = result.data.id,
                        title = result.data.title,
                        content = result.data.content
                    )

                    initMemoData = memoData

                    _memoData.value = memoData

                    _uiState.value = MemoDetailUiState.Finished
                } else {
                    val data = (result as com.nkot117.data.model.Result.Failure).errorMessage
                    _uiState.value = MemoDetailUiState.Error(data.message)
                }
            }
        } else {
            viewModelScope.launch {
                val result = memoRepository.getMemoDetail(id)
                if (result is com.nkot117.data.model.Result.Success) {
                    val memoData = MemoData(
                        id = result.data.id,
                        title = result.data.title,
                        content = result.data.content
                    )

                    initMemoData = memoData

                    _memoData.value = memoData

                    _uiState.value = MemoDetailUiState.Finished
                } else {
                    val data = (result as com.nkot117.data.model.Result.Failure).errorMessage
                    _uiState.value = MemoDetailUiState.Error(data.message)
                }
            }
        }

    }

    fun onTitleChanged(title: String) {
        _memoData.update { it.copy(title = title) }
    }

    fun onContentChanged(content: String) {
        _memoData.update { it.copy(content = content) }
    }

    fun saveMemo() {
        if (initMemoData == memoData.value) {
            return
        }

        viewModelScope.launch {
            val memo = memoData.value
            val result = memoRepository.updateMemo(
                id = memo.id,
                title = memo.title,
                content = memo.content
            )

            if (result is com.nkot117.data.model.Result.Success) {
                initMemoData = memo
            }
        }
    }
}