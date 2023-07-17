package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.ui.common.errorStateForDisplay
import fr.outadoc.woolly.ui.common.errorStateForNotification
import fr.outadoc.woolly.ui.common.itemPlaceholders
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    tagFlow: Flow<PagingData<Tag>>,
    lazyListState: LazyListState,
    onHashtagClick: (String) -> Unit = {},
    itemKey: ((Tag) -> String)? = { tag -> tag.name },
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = tagFlow.collectAsLazyPagingItems()

    lazyPagingItems.errorStateForNotification?.error?.let { error ->
        LaunchedEffect(error) {
            onLoadError(error, lazyPagingItems::retry)
        }
    }

    SwipeRefresh(
        onRefresh = lazyPagingItems::refresh,
        state = rememberSwipeRefreshState(
            isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
        )
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = insets
        ) {
            lazyPagingItems.errorStateForDisplay?.let { errorState ->
                item(key = "error") {
                    ErrorScreen(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        error = errorState.error,
                        onRetry = lazyPagingItems::retry
                    )
                }
            }

            itemPlaceholders(items = lazyPagingItems) {
                Column {
                    HashtagPlaceholder()
                    Divider(thickness = Dp.Hairline)
                }
            }

            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey(itemKey)
            ) { index ->
                val tag = lazyPagingItems[index]
                Column {
                    if (tag != null) {
                        HashtagListItem(
                            tag = tag,
                            onClick = { onHashtagClick(tag.name) }
                        )
                    }

                    Divider(thickness = Dp.Hairline)
                }
            }
        }
    }
}