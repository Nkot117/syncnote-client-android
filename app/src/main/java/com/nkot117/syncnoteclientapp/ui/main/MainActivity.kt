package com.nkot117.syncnoteclientapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nkot117.syncnoteclientapp.ui.auth.AuthScreen
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.ui.home.HomeScreen
import com.nkot117.syncnoteclientapp.ui.theme.SyncnoteClientAppTheme
import com.nkot117.syncnoteclientapp.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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

enum class MainScreen() {
    Home,
    Auth,
}

@Composable
fun SyncnoteClientApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    LogUtil.d("SyncnoteClientApp Composable")
    val uiState by viewModel.uiState.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.updateIsLogged()
    }

    NavHost(
        navController = navController,
        startDestination = MainScreen.Auth.name
    ) {
        composable(route = MainScreen.Home.name) {
            HomeScreen(
                moveAuthScreen = {
                    navController.navigate(MainScreen.Auth.name) {
                        popUpTo(0) { inclusive = true }
                    }
                    viewModel.logout()
                },
                logoutAction = {
                    viewModel.logout()
                }
            )
        }

        composable(route = MainScreen.Auth.name) {
            AuthScreen(
                moveHomeScreen = {
                    navController.navigate(MainScreen.Home.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }

    when (uiState) {
        is MainUiState.Loading -> {
            LogUtil.d("SyncnoteClientApp Loading")
            CustomLoadingScreen()
        }

        is MainUiState.Finished -> {
            LogUtil.d("SyncnoteClientApp Finished")
            LoggedInContent(navController, isUserLoggedIn)
        }
    }
}

@Composable
fun LoggedInContent(
    navController: NavController,
    isLogged: Boolean
) {
    LogUtil.d("LoggedInContent Composable")
    if (isLogged) {
        navController.navigate(MainScreen.Home.name)
    } else {
        navController.navigate(MainScreen.Auth.name)
    }
}
