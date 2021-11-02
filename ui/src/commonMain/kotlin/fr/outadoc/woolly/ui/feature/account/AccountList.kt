package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.clickable
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
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.ui.common.errorStateForDisplay
import fr.outadoc.woolly.ui.common.errorStateForNotification
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun AccountList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    accountFlow: Flow<PagingData<Account>>,
    lazyListState: LazyListState,
    onAccountClick: (Account) -> Unit = {},
    itemKey: ((Account) -> String)? = { account -> account.accountId },
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = accountFlow.collectAsLazyPagingItems()

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
                key = itemKey
            ) { account ->
                Column {
                    if (account != null) {
                        Account(
                            modifier = Modifier
                                .clickable { onAccountClick(account) }
                                .padding(16.dp),
                            account = account
                        )
                    } else {
                        AccountPlaceholder()
                    }

                    Divider(thickness = Dp.Hairline)
                }
            }
        }
    }
}
