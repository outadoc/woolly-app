package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.common.ListExtremityState
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun StatusList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    statusFlow: Flow<PagingData<Status>>,
    lazyListState: LazyListState,
    header: (@Composable () -> Unit)? = null,
    maxContentWidth: Dp = WoollyDefaults.MaxContentWidth,
    onStatusAction: (StatusAction) -> Unit = {},
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    itemKey: ((Status) -> String)? = { status -> status.statusId }
) {
    val lazyPagingItems = statusFlow.collectAsLazyPagingItems()

    SwipeRefresh(
        onRefresh = lazyPagingItems::refresh,
        state = rememberSwipeRefreshState(
            isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = modifier.widthIn(max = maxContentWidth),
                state = if (lazyPagingItems.hasRestoredItems) lazyListState else LazyListState(),
                contentPadding = insets
            ) {
                header?.let { header ->
                    item("header") {
                        header()
                    }
                }

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
                ) { status ->
                    Column {
                        if (status != null) {
                            StatusOrBoost(
                                modifier = Modifier
                                    .clickable { onStatusClick(status) }
                                    .padding(16.dp),
                                status = status,
                                onStatusAction = onStatusAction,
                                onAttachmentClick = onAttachmentClick,
                                onStatusReplyClick = onStatusReplyClick,
                                onAccountClick = onAccountClick
                            )
                        } else {
                            StatusPlaceholder()
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
}
