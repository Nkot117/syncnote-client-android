package com.nkot117.syncnoteclientapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoListScreen(
    viewModel: MemoListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMemos()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "メモ一覧",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is MemoListUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MemoListUiState.Success -> {

                    LazyColumn {
                        val memoList = (uiState as MemoListUiState.Success).memoList
                        items(memoList) {
                            memoListItem(
                                title = it.title,
                                content = it.content
                            )
                        }

                    }
                }

                is MemoListUiState.Error -> {
                    Text(
                        text = "Error"
                    )
                }
            }
        }
    }
}

@Composable
fun memoListItem(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MemoListItemPreview() {
    memoListItem(
        title = "タイトル",
        content = "内容"
    )
}
