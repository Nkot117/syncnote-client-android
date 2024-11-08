package com.nkot117.syncnoteclientapp.ui.memo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nkot117.syncnoteclientapp.ui.components.CustomLoadingScreen
import com.nkot117.syncnoteclientapp.ui.components.CustomOneButtonDialog
import com.nkot117.syncnoteclientapp.ui.memo.MemoData
import com.nkot117.syncnoteclientapp.util.LogUtil

@Composable
fun MemoListScreen(
    viewModel: MemoListViewModel = hiltViewModel(),
    memoClickAction: (id: String) -> Unit,
    memoAddAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    LogUtil.d("MemoListScreen Composable")
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMemos()
    }

    when (uiState) {
        is MemoListUiState.Loading -> {
            LogUtil.d("MemoListScreen Loading")
            CustomLoadingScreen(modifier)
        }

        is MemoListUiState.Success -> {
            LogUtil.d("MemoListScreen Success")
            MemoSuccessContent(
                memoList = (uiState as MemoListUiState.Success).memoList,
                memoClickAction = memoClickAction,
                memoAddAction = memoAddAction,
                memoDeleteAction = {
                    viewModel.deleteMemo(it)
                },
                isRefreshing = (uiState as MemoListUiState.Success).isRefreshing,
                refreshAction = {
                    viewModel.refreshMemos()
                },
                modifier = modifier
            )
        }

        is MemoListUiState.Error -> {
            LogUtil.d("MemoListScreen Error")
            CustomOneButtonDialog(
                title = "エラー",
                message = "メモを取得できませんでした。",
                button = "リトライ",
                onDismiss = {
                    viewModel.loadMemos()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoSuccessContent(
    memoList: List<MemoData>,
    modifier: Modifier = Modifier,
    memoClickAction: (id: String) -> Unit = {},
    memoAddAction: () -> Unit = {},
    memoDeleteAction: (id: String) -> Unit = {},
    isRefreshing: Boolean = false,
    refreshAction: () -> Unit = {},
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = {
            LogUtil.d("PullToRefreshBox onRefresh")
            refreshAction()
        }
    ) {
        MemoListContent(
            memoList = memoList,
            memoClickAction = memoClickAction,
            memoDeleteAction = {
                memoDeleteAction(it)
            },
            modifier = modifier
        )
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = {
                memoAddAction()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun MemoListContent(
    memoList: List<MemoData>,
    memoClickAction: (id: String) -> Unit,
    memoDeleteAction: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LogUtil.d("MemoListContent Composable")
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(memoList) {
            MemoListItem(
                title = it.title,
                content = it.content,
                cardClickAction = {
                    LogUtil.d("MemoListContent cardClickAction")
                    memoClickAction(it.id)
                },
                memoDeleteAction = {
                    LogUtil.d("MemoListContent onDeleteClick")
                    memoDeleteAction(it.id)
                }
            )
        }
    }
}

@Composable
fun MemoListItem(
    title: String,
    content: String,
    cardClickAction: () -> Unit,
    memoDeleteAction: () -> Unit,
) {
    LogUtil.d("MemoListItem Composable")
    Column(
        modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        Card(
            onClick = {
                cardClickAction()
            },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        maxLines = 1,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                    )
                }

                IconButton(
                    onClick = {
                        memoDeleteAction()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Gray
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoListScreenPreview() {
    MemoSuccessContent(
        memoList = List(10) {
            MemoData(
                id = it.toString(),
                title = "タイトル$it",
                content = "内容$it"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MemoListItemPreview() {
    MemoListItem(
        title = "タイトル",
        content = "内容",
        cardClickAction = {},
        memoDeleteAction = {}
    )
}
