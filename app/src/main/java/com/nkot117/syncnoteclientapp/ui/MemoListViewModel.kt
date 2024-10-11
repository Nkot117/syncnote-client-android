package com.nkot117.syncnoteclientapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.model.MemoResult
import com.nkot117.syncnoteclientapp.data.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val repository: MemoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MemoListUiState>(MemoListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMemos() {
        _uiState.value = MemoListUiState.Loading
        viewModelScope.launch {
            val result = repository.getMemoList()
            if (result is MemoResult.Success) {
                _uiState.value = MemoListUiState.Success(result.memoList.map {
                    MemoData(
                        title = it.title,
                        content = it.content
                    )
                })
            } else {
                val data = (result as MemoResult.Failure).errorMessage
                _uiState.value = MemoListUiState.Error(data.message)
            }
        }
    }
}
