package com.nkot117.syncnoteclientapp.ui.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkot117.syncnoteclientapp.data.model.Result
import com.nkot117.syncnoteclientapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Ideal)
    val uiState = _uiState.asStateFlow()

    fun clearUiState() {
        _uiState.value = AccountUiState.Ideal
    }

    fun deleteAccount() {
        _uiState.value = AccountUiState.Loading

        viewModelScope.launch {
            val result = repository.deleteAccount()
            if (result is Result.Success) {
                _uiState.value = AccountUiState.Success
            } else {
                _uiState.value = AccountUiState.Error
            }
        }
    }
}