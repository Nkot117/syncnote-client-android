package com.nkot117.syncnoteclientapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {
    private val _isLogged = MutableStateFlow(false)
    val isLogged = _isLogged.asStateFlow()

    init {
        _isLogged.value = true
    }
}
