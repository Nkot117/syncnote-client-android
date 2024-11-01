package com.nkot117.syncnoteclientapp.ui.memo.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.ui.memo.MemoData
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun MemoDetailScreen(
    viewModel: MemoDetailViewModel = hiltViewModel(),
    id: String?,
    modifier: Modifier = Modifier
) {
    LogUtil.d("MemoDetailScreen Composable id: $id")

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY || event == Lifecycle.Event.ON_PAUSE) {
                viewModel.saveMemo()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMemo(id)
    }

    when (uiState) {
        is MemoDetailUiState.Loading -> {
            LogUtil.d("MemoDetailScreen Loading")
            CustomLoadingScreen(modifier)
        }

        is MemoDetailUiState.Finished -> {
            LogUtil.d("MemoDetailScreen Success")
            val memo by viewModel.memoData.collectAsState()
            MemoDetailsFinishedContent(
                memo = memo,
                onTitleChanged = { viewModel.onTitleChanged(it) },
                onContentChanged = { viewModel.onContentChanged(it) },
                onSaveMemo = { viewModel.saveMemo() },
                modifier = modifier
            )

        }

        is MemoDetailUiState.Error -> {
            val message = (uiState as MemoDetailUiState.Error).message
            Text(text = message)
        }
    }
}

@Composable
fun MemoDetailsFinishedContent(
    memo: MemoData,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onSaveMemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = memo.title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (isFocused && !it.isFocused) {
                        onSaveMemo()
                    }
                    isFocused = it.isFocused
                },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = memo.content,
            onValueChange = onContentChanged,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onFocusChanged {
                    if (isFocused && !it.isFocused) {
                        onSaveMemo()
                    }
                    isFocused = it.isFocused
                },
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemoDetailFinishedPreview() {
    MemoDetailsFinishedContent(
        memo = MemoData(
            id = "1",
            title = "Title",
            content = "Content"
        ),
        onTitleChanged = {},
        onContentChanged = {},
        onSaveMemo = {}
    )
}