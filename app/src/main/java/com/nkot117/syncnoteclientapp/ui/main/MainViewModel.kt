package com.nkot117.syncnoteclientapp.ui.main

import androidx.lifecycle.ViewModel
import com.nkot117.syncnoteclientapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isLogged = MutableStateFlow(false)
    val isLogged = _isLogged.asStateFlow()

    init {
        val token = authRepository.getToken()
        _isLogged.value = token.isNotEmpty()
    }

    fun updateIsLogged() {
        _uiState.value = MainUiState.Loading
        val token = authRepository.getToken()
        _isLogged.value = token.isNotEmpty()
        _uiState.value = MainUiState.Finished
    }
}
