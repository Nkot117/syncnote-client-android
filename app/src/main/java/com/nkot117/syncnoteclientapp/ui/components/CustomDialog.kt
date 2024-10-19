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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomDialog(
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

@Preview
@Composable
fun CustomDialogPreview() {
    CustomDialog(title ="タイトル", message = "ログインできませんでした", button = "OK", onDismiss = {})
}
