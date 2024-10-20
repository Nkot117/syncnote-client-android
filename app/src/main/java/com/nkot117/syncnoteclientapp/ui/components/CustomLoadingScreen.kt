package com.nkot117.syncnoteclientapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun CustomLoadingScreen(
    modifier: Modifier = Modifier
) {
    LogUtil.d("LoadingScreen Composable")
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomLoadingScreen() {
    CustomLoadingScreen()
}