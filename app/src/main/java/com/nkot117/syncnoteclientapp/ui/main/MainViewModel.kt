package com.nkot117.syncnoteclientapp.ui.main

import androidx.lifecycle.ViewModel
import com.nkot117.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: com.nkot117.data.repository.AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()

    init {
        val token = authRepository.getToken()
        _isUserLoggedIn.value = token.isNotEmpty()
    }

    fun updateIsLogged() {
        val token = authRepository.getToken()
        _isUserLoggedIn.value = token.isNotEmpty()
        _uiState.value = MainUiState.Finished
    }

    fun logout() {
        authRepository.clearTokens()
        _isUserLoggedIn.value = false
    }
}
