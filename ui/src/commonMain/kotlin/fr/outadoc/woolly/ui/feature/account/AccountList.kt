package fr.outadoc.woolly.ui.feature.account

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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.ui.common.ListExtremityState
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import kotlinx.coroutines.flow.Flow

@Composable
fun AccountList(
    modifier: Modifier = Modifier,
    insets: PaddingValues = PaddingValues(),
    accountFlow: Flow<PagingData<Account>>,
    lazyListState: LazyListState,
    onAccountClick: (Account) -> Unit = {}
) {
    val lazyPagingItems = accountFlow.collectAsLazyPagingItems()

    SwipeRefresh(
        onRefresh = lazyPagingItems::refresh,
        state = rememberSwipeRefreshState(
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

            items(
                items = lazyPagingItems,
                key = { account -> account.accountId }
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
