package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.ui.common.ListExtremityState
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    tagFlow: Flow<PagingData<Tag>>,
    lazyListState: LazyListState,
    onHashtagClick: (String) -> Unit = {},
    itemKey: ((Tag) -> String)? = { tag -> tag.name }
) {
    val lazyPagingItems = tagFlow.collectAsLazyPagingItems()

    SwipeRefresh(
        onRefresh = lazyPagingItems::refresh,
        state = rememberSwipeRefreshState(
            isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
        )
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = if (lazyPagingItems.hasRestoredItems) lazyListState else LazyListState(),
            contentPadding = insets
        ) {
            when (val state = lazyPagingItems.loadState.refresh) {
                is LoadState.Error -> item(key = "error") {
                    ErrorScreen(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        error = state.error,
                        onRetry = lazyPagingItems::retry
                    )
                }
            }

            item(key = "prepend") {
                ListExtremityState(
                    state = lazyPagingItems.loadState.prepend,
                    onRetry = lazyPagingItems::retry,
                    animationDirection = Alignment.Top
                )
            }

            items(
                items = lazyPagingItems,
                key = itemKey
            ) { tag ->
                Column {
                    if (tag != null) {
                        HashtagListItem(
                            tag = tag,
                            onClick = { onHashtagClick(tag.name) }
                        )
                    } else {
                        HashtagPlaceholder()
                    }

                    Divider(thickness = Dp.Hairline)
                }
            }

            item(key = "append") {
                ListExtremityState(
                    state = lazyPagingItems.loadState.append,
                    onRetry = lazyPagingItems::retry,
                    animationDirection = Alignment.Bottom
                )
            }
        }
    }
}