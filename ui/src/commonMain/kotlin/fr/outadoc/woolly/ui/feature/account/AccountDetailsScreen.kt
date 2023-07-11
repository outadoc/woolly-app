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
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent.State
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun AccountDetailsScreen(
    component: AccountDetailsComponent,
    insets: PaddingValues = PaddingValues(),
    accountId: String,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
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
        when (val currentState = state) {
            is State.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    error = currentState.exception,
                    onRetry = component::refresh
                )
            }

            is State.LoadedAccount -> {
                StatusList(
                    modifier = Modifier.padding(insets),
                    statusFlow = component.timelinePagingItems,
                    lazyListState = component.listState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick,
                    onStatusAction = { action ->
                        component.onStatusAction(action)
                    },
                    onStatusReplyClick = onStatusReplyClick,
                    onAccountClick = onAccountClick,
                    header = {
                        AccountHeader(
                            modifier = Modifier.padding(bottom = 8.dp),
                            account = currentState.account,
                            isFollowing = currentState.relationship.isFollowing,
                            onFollowClick = { follow ->
                                component.onFollowClick(follow)
                            }
                        )
                    },
                    onLoadError = onLoadError
                )
            }

            is State.Initial -> {}
        }
    }
}
