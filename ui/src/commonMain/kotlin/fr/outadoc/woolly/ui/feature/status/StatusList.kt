package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
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
    onAccountClick: (Account) -> Unit = {}
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
                state = lazyListState,
                contentPadding = insets
            ) {
                header?.let { header ->
                    item("header") {
                        header()
                    }
                }

                when (val state = lazyPagingItems.loadState.refresh) {
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

                items(
                    items = lazyPagingItems,
                    key = { status -> status.statusId }
                ) { status ->
                    Column {
                        if (status != null) {
                            StatusOrBoost(
                                modifier = Modifier
                                    .clickable { onStatusClick(status) }
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                status = status,
                                onStatusAction = onStatusAction,
                                onAttachmentClick = onAttachmentClick,
                                onStatusReplyClick = onStatusReplyClick,
                                onAccountClick = onAccountClick
                            )
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
    }
}
