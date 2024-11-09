package com.nkot117.syncnoteclientapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomOneButtonDialog(
    title: String = "",
    message: String,
    button: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            if(title.isNotEmpty()) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = button,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun CustomTwoButtonDialog(
    title: String = "",
    message: String =  "",
    positiveButton: String,
    negativeButton: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
    positiveButtonColor : Color = MaterialTheme.colorScheme.primary,
    negativeButtonColor : Color = MaterialTheme.colorScheme.secondary
) {
    AlertDialog(
        onDismissRequest = { onNegative() },
        title = {
            if(title.isNotEmpty()) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            Button(
                onClick = { onPositive() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = positiveButtonColor
                )
            ) {
                Text(
                    text = positiveButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onNegative() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = negativeButtonColor
                )
            ) {
                Text(
                    text = negativeButton,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Preview
@Composable
fun CustomOneButtonDialogPreview() {
    CustomOneButtonDialog(title ="タイトル", message = "ログインできませんでした", button = "OK", onDismiss = {})
}

@Preview
@Composable
fun CustomTwoButtonDialogPreview() {
    CustomTwoButtonDialog(title ="タイトル", message = "ログアウトしますか？", positiveButton = "ログアウト", negativeButton = "キャンセル", onPositive = {}, onNegative = {})
}
