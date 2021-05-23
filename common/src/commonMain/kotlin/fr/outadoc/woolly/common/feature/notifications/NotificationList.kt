package fr.outadoc.woolly.common.feature.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.account.ui.Account
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.feature.status.ui.RelativeTime
import fr.outadoc.woolly.common.feature.status.ui.Status
import fr.outadoc.woolly.common.ui.ListExtremityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun NotificationList(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    notificationFlow: Flow<PagingData<Notification>>,
    lazyListState: LazyListState
) {
    // Periodically refresh timestamps
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    rememberCoroutineScope().launch(Dispatchers.Default) {
        while (isActive) {
            delay(1_000)
            currentTime = Clock.System.now()
        }
    }

    val lazyPagingItems = notificationFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = insets
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

        itemsIndexed(lazyPagingItems) { _, notification ->
            Column {
                if (notification != null) {
                    key(notification.notificationId) {
                        Notification(
                            modifier = Modifier.fillMaxWidth(),
                            notification = notification,
                            currentTime = currentTime
                        )
                    }
                } else {
                    NotificationPlaceHolder()
                }

                TabRowDefaults.Divider(thickness = 1.dp)
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
fun NotificationPlaceHolder() {
    Spacer(modifier = Modifier.height(128.dp))
}

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    notification: Notification,
    currentTime: Instant?
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .clickable {
                uriHandler.openUri(notification.status?.url ?: notification.account.url)
            }
            .padding(16.dp)
    ) {
        NotificationHeader(
            modifier = Modifier.padding(bottom = 16.dp),
            notification = notification,
            currentTime = currentTime
        )

        when (val status = notification.status) {
            null -> AccountNotificationBody(account = notification.account)
            else -> StatusNotificationBody(status = status)
        }
    }
}

@Composable
fun AccountNotificationBody(
    modifier: Modifier = Modifier,
    account: Account
) {
    Account(modifier = modifier, account = account)
}

@Composable
fun StatusNotificationBody(
    modifier: Modifier = Modifier,
    status: Status
) {
    Status(modifier = modifier, status = status)
}

@Composable
fun NotificationHeader(
    modifier: Modifier = Modifier,
    notification: Notification,
    currentTime: Instant?
) {
    val accountTitle = notification.account.displayNameOrAcct
    val title = when (notification.type) {
        NotificationType.Follow -> "$accountTitle follows you"
        NotificationType.FollowRequest -> "$accountTitle sent you a follow request"
        NotificationType.Mention -> "$accountTitle mentioned you"
        NotificationType.Boost -> "$accountTitle boosted your post"
        NotificationType.Favourite -> "$accountTitle favourited your post"
        NotificationType.Poll -> "A poll has ended"
        NotificationType.Status -> "$accountTitle posted something"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier,
            text = title,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )

        currentTime?.let {
            RelativeTime(
                modifier = Modifier.alignByBaseline(),
                currentTime = currentTime,
                time = notification.createdAt,
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
    }
}
