package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent.State
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun AccountDetailsScreen(
    component: AccountDetailsComponent,
    insets: PaddingValues = PaddingValues(),
    accountId: String
) {
    val state by component.state.collectAsState(State.Initial())

    LaunchedEffect(accountId) {
        component.loadAccount(accountId)
    }

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        onRefresh = component::refresh,
        state = rememberSwipeRefreshState(
            isRefreshing = state.isLoading
        )
    ) {
        when (val state = state) {
            is State.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    error = state.exception,
                    onRetry = component::refresh
                )
            }
            is State.LoadedAccount -> {
                StatusList(
                    modifier = Modifier.padding(insets),
                    statusFlow = component.timelinePagingItems,
                    lazyListState = component.listState,
                    header = {
                        AccountHeader(
                            modifier = Modifier.padding(bottom = 8.dp),
                            account = state.account,
                            isFollowing = state.relationship.isFollowing,
                            onFollowClick = { follow ->
                                component.onFollowClick(follow)
                            }
                        )
                    }
                )
            }
        }
    }
}
