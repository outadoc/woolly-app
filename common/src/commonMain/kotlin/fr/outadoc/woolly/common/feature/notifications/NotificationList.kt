package fr.outadoc.woolly.common.feature.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.feature.status.ui.ProfilePicture
import fr.outadoc.woolly.common.feature.status.ui.Status
import fr.outadoc.woolly.common.ui.ListExtremityState
import fr.outadoc.woolly.common.ui.WoollyDefaults
import fr.outadoc.woolly.common.ui.WoollyTheme
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
    lazyListState: LazyListState,
    maxContentWidth: Dp = WoollyDefaults.MaxContentWidth
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = modifier.widthIn(max = maxContentWidth),
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

            itemsIndexed(
                items = lazyPagingItems,
                key = { _, notification -> notification.notificationId }
            ) { _, notification ->
                Column {
                    if (notification != null) {
                        Notification(
                            modifier = Modifier.fillMaxWidth(),
                            notification = notification,
                            currentTime = currentTime
                        )
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
        if (notification.type != NotificationType.Mention) {
            NotificationHeader(
                modifier = Modifier
                    .padding(bottom = if (notification.status != null) 16.dp else 0.dp),
                notification = notification
            )
        }

        notification.status?.let { status ->
            StatusNotificationBody(
                status = status,
                currentTime = currentTime
            )
        }
    }
}

@Composable
fun StatusNotificationBody(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant?
) {
    Status(
        modifier = modifier,
        status = status,
        currentTime = currentTime
    )
}

@Composable
fun NotificationHeader(
    modifier: Modifier = Modifier,
    notification: Notification
) {
    val uriHandler = LocalUriHandler.current
    val startPadding = WoollyDefaults.AvatarSize + 16.dp

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.width(startPadding),
                horizontalAlignment = Alignment.End
            ) {
                NotificationIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    notification = notification
                )
            }

            ProfilePicture(
                modifier = Modifier.size(32.dp),
                account = notification.account,
                onClick = { uriHandler.openUri(notification.account.url) }
            )
        }

        val accountTitle = notification.account.displayNameOrAcct
        Text(
            modifier = Modifier.padding(
                start = startPadding,
                top = 8.dp
            ),
            text = when (notification.type) {
                NotificationType.Follow -> "$accountTitle follows you"
                NotificationType.FollowRequest -> "$accountTitle would like to follow you"
                NotificationType.Mention -> "New mention"
                NotificationType.Boost -> "$accountTitle boosted your post"
                NotificationType.Favourite -> "$accountTitle favourited your post"
                NotificationType.Poll -> "A poll has ended"
                NotificationType.Status -> "New post"
            },
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NotificationIcon(modifier: Modifier = Modifier, notification: Notification) {
    Box(modifier) {
        when (notification.type) {
            NotificationType.Follow -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = "New follower",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.FollowRequest -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = "New follow request",
                tint = MaterialTheme.colors.secondary
            )
            NotificationType.Mention -> Icon(
                Icons.Default.Comment,
                contentDescription = "New mention",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Boost -> Icon(
                Icons.Default.Repeat,
                contentDescription = "New boost",
                tint = WoollyTheme.BoostColor
            )
            NotificationType.Favourite -> Icon(
                Icons.Default.Star,
                contentDescription = "New favourite",
                tint = WoollyTheme.FavouriteColor
            )
            NotificationType.Poll -> Icon(
                Icons.Default.Poll,
                contentDescription = "Poll results are in",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Status -> Icon(
                Icons.Default.Inbox,
                contentDescription = "New post",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
