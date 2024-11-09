package com.nkot117.syncnoteclientapp.ui.home.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.ui.components.CustomOneButtonDialog
import com.nkot117.syncnoteclientapp.ui.components.CustomTwoButtonDialog

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is AccountUiState.Ideal -> AccountScreenContent(modifier, onLogout, viewModel::deleteAccount)
        is AccountUiState.Loading -> CustomLoadingScreen(modifier)
        is AccountUiState.Error -> AccountScreenError(viewModel::clearUiState)
        is AccountUiState.Success -> AccountScreenSuccess(onLogout)
    }
}

@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    var isShowLogoutDialog by remember { mutableStateOf(false) }
    var isShowDeleteAccountDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        LogoutListItem {
            isShowLogoutDialog = true
        }
        HorizontalDivider()
        DeleteAccountListItem {
            isShowDeleteAccountDialog = true
        }
    }

    if (isShowLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                onLogout()
                isShowLogoutDialog = false
            },
            onDismiss = {
                isShowLogoutDialog = false
            }
        )
    }

    if (isShowDeleteAccountDialog) {
        DeleteAccountDialog(
            onConfirm = {
                onDeleteAccount()
                isShowDeleteAccountDialog = false
            },
            onDismiss = {
                isShowDeleteAccountDialog = false
            }
        )
    }
}

@Composable
fun LogoutListItem(onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("ログアウト") },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun DeleteAccountListItem(onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("アカウント削除") },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomTwoButtonDialog(
        title = "ログアウト",
        message = "ログアウトしますか？",
        positiveButton = "ログアウト",
        negativeButton = "キャンセル",
        onPositive = onConfirm,
        onNegative = onDismiss
    )
}

@Composable
fun DeleteAccountDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    CustomTwoButtonDialog(
        title = "アカウント削除",
        message = "アカウントを削除しますか？\n削除するとメモが全て消去されます。",
        positiveButton = "削除",
        negativeButton = "キャンセル",
        onPositive = onConfirm,
        onNegative = onDismiss,
        positiveButtonColor = MaterialTheme.colorScheme.error
    )
}

@Composable
fun AccountScreenError(
    onDismiss: () -> Unit
) {
    CustomOneButtonDialog(
        title = "エラー",
        message = "アカウントの削除に失敗しました。\nしばらく時間をおいてから、もう一度お試しください。",
        button = "OK",
        onDismiss = {
            onDismiss()
        }
    )
}

@Composable
fun AccountScreenSuccess(onLogout: () -> Unit) {
    CustomOneButtonDialog(
        title = "アカウント削除",
        message = "アカウントを削除しました。",
        button = "OK",
        onDismiss = { onLogout() }
    )
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    AccountScreenContent(
        onLogout = {},
        onDeleteAccount = {}
    )
}