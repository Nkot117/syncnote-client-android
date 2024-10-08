package com.nkot117.syncnoteclientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nkot117.syncnoteclientapp.ui.auth.login.LoginScreen
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
fun SyncnoteClientApp(modifier: Modifier = Modifier) {
    LoginScreen(modifier = modifier)
}