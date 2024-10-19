package com.nkot117.syncnoteclientapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.MemoListScreen
import com.nkot117.syncnoteclientapp.ui.auth.AuthScreen
import com.nkot117.syncnoteclientapp.ui.theme.SyncnoteClientAppTheme
import com.nkot117.syncnoteclientapp.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.d("MainActivity onCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SyncnoteClientAppTheme {
                SyncnoteClientApp()
            }
        }
    }
}

@Composable
fun SyncnoteClientApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    LogUtil.d("SyncnoteClientApp Composable")
    val uiState by viewModel.uiState.collectAsState()
    val isLogged by viewModel.isLogged.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateIsLogged()
    }

    when (uiState) {
        is MainUiState.Loading -> {
            LogUtil.d("SyncnoteClientApp Loading")
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is MainUiState.Finished -> {
            LogUtil.d("SyncnoteClientApp Finished")
            if (isLogged) {
                MemoListScreen()
            } else {
                AuthScreen()
            }
        }
    }

}
