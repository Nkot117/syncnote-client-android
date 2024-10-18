package com.nkot117.syncnoteclientapp

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
    private val _isLogged = MutableStateFlow(false)
    val isLogged = _isLogged.asStateFlow()

    init {
        val token = authRepository.getToken()
        _isLogged.value = token.isNotEmpty()
    }
}
