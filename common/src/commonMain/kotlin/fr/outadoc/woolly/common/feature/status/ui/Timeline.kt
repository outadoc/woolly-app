package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.plus
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    statusFlow: Flow<PagingData<Status>>,
    lazyListState: LazyListState,
    onStatusAction: (StatusAction) -> Unit = {}
) {
    // Periodically refresh timestamps
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    rememberCoroutineScope().launch(Dispatchers.Default) {
        while (isActive) {
            delay(1_000)
            currentTime = Clock.System.now()
        }
    }

    val lazyPagingItems = statusFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = insets + PaddingValues(top = 8.dp, bottom = 8.dp)
    ) {
        when (val state = lazyPagingItems.loadState.refresh) {
            LoadState.Loading -> item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> item {
                ErrorScreen(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(16.dp),
                    error = state.error,
                    onRetry = lazyPagingItems::retry
                )
            }
        }

        item {
            ListExtremityState(
                state = lazyPagingItems.loadState.prepend,
                onRetry = lazyPagingItems::retry
            )
        }

        itemsIndexed(lazyPagingItems) { _, status ->
            Column {
                if (status != null) {
                    key(status.statusId) {
                        StatusOrBoost(
                            status = status,
                            currentTime = currentTime,
                            onStatusAction = onStatusAction
                        )
                    }
                } else {
                    StatusPlaceholder()
                }

                Divider(thickness = 1.dp)
            }
        }

        item {
            ListExtremityState(
                state = lazyPagingItems.loadState.append,
                onRetry = lazyPagingItems::retry
            )
        }
    }
}

@Composable
fun LazyItemScope.ListExtremityState(state: LoadState, onRetry: () -> Unit) {
    when (state) {
        is LoadState.Loading -> {
            Column(
                modifier = Modifier.fillParentMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            ErrorScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                error = state.error,
                onRetry = onRetry
            )
        }
    }
}
