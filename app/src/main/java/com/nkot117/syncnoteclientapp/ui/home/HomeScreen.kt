package com.nkot117.syncnoteclientapp.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nkot117.syncnoteclientapp.ui.memo.MemoListScreen
import com.nkot117.syncnoteclientapp.ui.memo.detail.MemoDetailScreen

sealed class HomeNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object MemoList : HomeNavItem("home", Icons.Default.Home, "Home")
    data object Account : HomeNavItem("Account", Icons.Default.AccountCircle, "Account")
}

sealed class MemoDetailNav(val route: String) {
    data object Detail : MemoDetailNav("detail")

}

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeNavItem.MemoList.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(HomeNavItem.MemoList.route) {
                MemoListScreen(
                    memoClickAction = {
                        navController.navigate(MemoDetailNav.Detail.route)
                    }
                )
            }

            composable(HomeNavItem.Account.route) { Text(text = "account") }

            composable(MemoDetailNav.Detail.route) {
                MemoDetailScreen()
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        HomeNavItem.MemoList,
        HomeNavItem.Account
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute == MemoDetailNav.Detail.route) return
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}