package com.nkot117.syncnoteclientapp.ui.memo.detail

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
class MemoDetailViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MemoDetailUiState>(MemoDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMemo(id: String?) {
        if(id == null){
            _uiState.value = MemoDetailUiState.Error("id is null")
            return
        }

        _uiState.value = MemoDetailUiState.Loading

        viewModelScope.launch {
            val result = memoRepository.getMemoDetail(id)
            if (result is Result.Success) {
                val data = result.data
                _uiState.value = MemoDetailUiState.Finished(
                    MemoData(
                        id = data.id,
                        title = data.title,
                        content = data.content
                    )
                )
            } else {
                val data = (result as Result.Failure).errorMessage
                _uiState.value = MemoDetailUiState.Error(data.message)
            }
        }
    }
}