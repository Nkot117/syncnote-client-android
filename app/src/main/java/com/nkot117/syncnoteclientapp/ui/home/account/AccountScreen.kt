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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nkot117.syncnoteclientapp.ui.components.CustomTwoButtonDialog

@Preview(showBackground = true)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    ) {
    var isShowLogoutDialog by remember { mutableStateOf(false) }
    var isShowDeleteAccountDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        ListItem(
            headlineContent = {
                Text("ログアウト")
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            },
            modifier = Modifier.clickable {
                 isShowLogoutDialog = true
            }
        )

        HorizontalDivider()

        ListItem(
            headlineContent = {
                Text("アカウント削除")
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            },
            modifier = Modifier.clickable {
                 isShowDeleteAccountDialog = true
            }
        )
    }

    if(isShowLogoutDialog) {
        CustomTwoButtonDialog(
            title = "ログアウト",
            message = "ログアウトしますか？",
            positiveButton = "ログアウト",
            negativeButton = "キャンセル",
            onPositive = {
                onLogout()
                isShowLogoutDialog = false
            },
            onNegative = {
                isShowLogoutDialog = false
            },
        )
    }

    if(isShowDeleteAccountDialog) {
        CustomTwoButtonDialog(
            title = "アカウント削除",
            message = "アカウントを削除しますか？\n削除するとメモが全て消去されます。",
            positiveButton = "削除",
            negativeButton = "キャンセル",
            onPositive = {
                isShowDeleteAccountDialog = false
            },
            onNegative = {
                isShowDeleteAccountDialog = false
            },
            positiveButtonColor = MaterialTheme.colorScheme.error
        )
    }
}