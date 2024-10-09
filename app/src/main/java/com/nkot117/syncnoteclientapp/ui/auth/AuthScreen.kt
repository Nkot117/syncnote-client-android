package com.nkot117.syncnoteclientapp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nkot117.syncnoteclientapp.ui.auth.login.LoginScreen
import com.nkot117.syncnoteclientapp.ui.auth.register.RegisterScreen

enum class AuthScreen() {
    Login,
    Register
}

@Composable
fun AuthScreen(
    modifier: Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Login.name
    ) {
        composable(route = AuthScreen.Login.name) {
            LoginScreen(
                moveRegisterScreen = {
                    navController.navigate(AuthScreen.Register.name)
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
    }
}