package com.nkot117.syncnoteclientapp.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nkot117.syncnoteclientapp.ui.memo.MemoListScreen
import com.nkot117.syncnoteclientapp.ui.memo.detail.MemoDetailScreen

sealed class HomeNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object MemoList : HomeNavItem("home", Icons.Default.Home, "Home")
    data object Account : HomeNavItem("Account", Icons.Default.AccountCircle, "Account")
}

sealed class MemoDetailNav(
    val route: String,
    val routeWithArgs: String,
    val argument: List<NamedNavArgument>
) {
    data object Detail : MemoDetailNav(
        route = "detail",
        routeWithArgs = "detail/{id}",
        argument = listOf(navArgument("id") { type = NavType.StringType }),
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            AppTopBar(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onAccountClick = { navController.navigate(HomeNavItem.Account.route) },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(MemoDetailNav.Detail.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
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
                        navController.navigate("${MemoDetailNav.Detail.route}/${it}")
                    }
                )
            }

            composable(HomeNavItem.Account.route) { Text(text = "account") }

            composable(
                route = MemoDetailNav.Detail.routeWithArgs,
                arguments = MemoDetailNav.Detail.argument
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("id")
                MemoDetailScreen(id = id)
            }

            composable(
                route = MemoDetailNav.Detail.route
            ) {
                MemoDetailScreen(id = null)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppTopBar(
    navController: NavHostController = rememberNavController(),
    onBackClick: () -> Unit = { },
    onAccountClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val canBack = currentRoute != null && currentRoute != HomeNavItem.MemoList.route
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "SyncnoteApp",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (canBack) {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = modifier.size(28.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                onAccountClick()
            }) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Account",
                    modifier = modifier.size(28.dp)
                )
            }
        }
    )
}
