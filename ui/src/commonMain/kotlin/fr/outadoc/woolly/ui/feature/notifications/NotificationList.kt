package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.*
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
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.errorStateForDisplay
import fr.outadoc.woolly.ui.common.errorStateForNotification
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun NotificationList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    notificationFlow: Flow<PagingData<Notification>>,
    lazyListState: LazyListState,
    maxContentWidth: Dp = WoollyDefaults.MaxContentWidth,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = notificationFlow.collectAsLazyPagingItems()

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
                    key = { notification -> notification.notificationId }
                ) { notification ->
                    Column {
                        if (notification != null) {
                            Notification(
                                modifier = Modifier.fillMaxWidth(),
                                notification = notification,
                                onStatusClick = onStatusClick,
                                onAttachmentClick = onAttachmentClick,
                                onAccountClick = onAccountClick
                            )
                        } else {
                            NotificationPlaceHolder()
                        }

                        Divider(thickness = Dp.Hairline)
                    }
                }
            }
        }
    }
}
