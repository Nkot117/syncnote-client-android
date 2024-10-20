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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun MemoDetailScreen(
    viewModel: MemoDetailViewModel = hiltViewModel(),
    id: String?,
    modifier: Modifier = Modifier
) {
    LogUtil.d("MemoDetailScreen Composable id: $id")
    val uiState by viewModel.uiState.collectAsState()
    val memo by viewModel.memoData.collectAsState()
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
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                TextField(
                    value = memo.title,
                    onValueChange = {
                        viewModel.onTitleChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (!it.isFocused) {
                                viewModel.saveMemo()
                            }
                        },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 24.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = memo.content,
                    onValueChange = {
                        viewModel.onContentChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .onFocusChanged {
                            if (!it.isFocused) {
                                viewModel.saveMemo()
                            }
                        },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                )
            }
        }

        is MemoDetailUiState.Error -> {
            val message = (uiState as MemoDetailUiState.Error).message
            Text(text = message)
        }
    }


}