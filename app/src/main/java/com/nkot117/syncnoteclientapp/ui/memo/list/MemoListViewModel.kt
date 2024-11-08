package com.nkot117.syncnoteclientapp.ui.memo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.repository.MemoRepository
import com.nkot117.syncnoteclientapp.ui.memo.MemoData
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
            if (result is Result.Success) {
                _uiState.value = MemoListUiState.Success(memoList = result.data.map {
                    MemoData(
                        id = it.id,
                        title = it.title,
                        content = it.content
                    )
                }, isRefreshing = false)
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = MemoListUiState.Error(data.message)
            }
        }
    }

    fun deleteMemo(id: String) {
        viewModelScope.launch {
            val result = repository.deleteMemo(id)
            if (result is Result.Success) {
                loadMemos()
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = MemoListUiState.Error(data.message)
            }
        }
    }

    fun refreshMemos() {
        val currentState = uiState.value
        if(currentState !is MemoListUiState.Success) return

        _uiState.value = currentState.copy(isRefreshing = true)

        viewModelScope.launch {
            val result = repository.getMemoList()
            if (result is Result.Success) {
                _uiState.value = MemoListUiState.Success(memoList = result.data.map {
                    MemoData(
                        id = it.id,
                        title = it.title,
                        content = it.content
                    )
                }, isRefreshing = false)
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = MemoListUiState.Error(data.message)
            }
        }
    }
}
