package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.errorStateForDisplay
import fr.outadoc.woolly.ui.common.errorStateForNotification
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
    itemKey: ((Status) -> String)? = { status -> status.statusId },
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = statusFlow.collectAsLazyPagingItems()

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
                        Surface(elevation = 1.dp) {
                            header()
                        }
                    }
                }

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
            }
        }
    }
}
