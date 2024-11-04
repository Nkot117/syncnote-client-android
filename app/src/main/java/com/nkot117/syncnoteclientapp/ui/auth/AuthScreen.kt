package com.nkot117.syncnoteclientapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nkot117.syncnoteclientapp.ui.auth.login.LoginScreen
import com.nkot117.syncnoteclientapp.ui.auth.register.RegisterScreen
import com.nkot117.syncnoteclientapp.ui.home.HomeScreen
import com.nkot117.syncnoteclientapp.util.LogUtil

enum class AuthScreen() {
    Login,
    Register,
    Home
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier
) {
    LogUtil.d("AuthScreen")
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.name,
        modifier = modifier
    ) {
        composable(route = AuthScreen.Login.name) {
            LoginScreen(
                moveRegisterScreen = {
                    navController.navigate(AuthScreen.Register.name)
                },
                moveHomeScreen = {
                    navController.navigate(AuthScreen.Home.name){
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AuthScreen.Register.name) {
            RegisterScreen(
                moveLoginScreen = {
                    navController.navigate(AuthScreen.Login.name)
                }
            )
        }

        composable(route = AuthScreen.Home.name) {
             HomeScreen()
        }
    }
}