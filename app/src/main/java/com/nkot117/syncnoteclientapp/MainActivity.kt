package com.nkot117.syncnoteclientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.auth.AuthScreen
import com.nkot117.syncnoteclientapp.ui.theme.SyncnoteClientAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
    val isLogged by viewModel.isLogged.collectAsState()

    if (isLogged) {
        Text(text = "Logged in")
    } else {
        AuthScreen()
    }
}
