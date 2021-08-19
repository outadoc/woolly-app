package fr.outadoc.woolly.common.feature.account.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.ui.ListExtremityState
import kotlinx.coroutines.flow.Flow

@Composable
fun AccountList(
    modifier: Modifier = Modifier,
    insets: PaddingValues,
    accountFlow: Flow<PagingData<Account>>,
    lazyListState: LazyListState
) {
    val uriHandler = LocalUriHandler.current
    val lazyPagingItems = accountFlow.collectAsLazyPagingItems()

    com.google.accompanist.swiperefresh.SwipeRefresh(
        onRefresh = lazyPagingItems::refresh,
        state = com.google.accompanist.swiperefresh.rememberSwipeRefreshState(
            isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
        )
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = insets
        ) {
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

            itemsIndexed(
                items = lazyPagingItems,
                key = { _, account -> account.accountId }
            ) { _, account ->
                Column {
                    if (account != null) {
                        Account(
                            modifier = Modifier
                                .clickable { uriHandler.openUri(account.url) }
                                .padding(16.dp),
                            account = account
                        )
                    } else {
                        AccountPlaceholder()
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
