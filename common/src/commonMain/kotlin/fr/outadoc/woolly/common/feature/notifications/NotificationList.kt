package fr.outadoc.woolly.common.feature.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.feature.status.ui.ListExtremityState
import fr.outadoc.woolly.common.plus
import kotlinx.coroutines.flow.Flow

@Composable
fun NotificationList(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    notificationFlow: Flow<PagingData<Notification>>,
    lazyListState: LazyListState
) {
    val lazyPagingItems = notificationFlow.collectAsLazyPagingItems()

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

        itemsIndexed(lazyPagingItems) { _, notification ->
            Column {
                if (notification != null) {
                    key(notification.notificationId) {
                        Notification(
                            modifier = Modifier.padding(16.dp),
                            notification = notification
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
    notification: Notification
) {
    Text(
        modifier = modifier,
        text = "${notification.type} by ${notification.account.displayNameOrAcct}"
    )
}
