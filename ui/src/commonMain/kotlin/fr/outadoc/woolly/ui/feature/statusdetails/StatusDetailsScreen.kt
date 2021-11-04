package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.ui.common.itemPlaceholders
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.StatusPlaceholder

@Composable
fun StatusDetailsScreen(
    component: StatusDetailsComponent,
    statusId: String,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val state by component.state.collectAsState(StatusDetailsComponent.State.Initial())

    LaunchedEffect(statusId) {
        component.loadStatus(statusId)
    }

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        onRefresh = component::refresh,
        state = rememberSwipeRefreshState(
            isRefreshing = state.isLoading
        )
    ) {
        if (state !is StatusDetailsComponent.State.LoadedStatus && state.isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(insets)
            ) {
                item {
                    Column {
                        StatusDetailsPlaceholder(
                            modifier = Modifier.padding(16.dp)
                        )

                        Divider(thickness = Dp.Hairline)
                    }
                }

                itemPlaceholders {
                    Column {
                        StatusPlaceholder(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )

                        Divider(thickness = Dp.Hairline)
                    }
                }
            }
        }

        when (val state = state) {
            is StatusDetailsComponent.State.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = component::refresh
                )
            }
            is StatusDetailsComponent.State.LoadedStatus -> {
                StatusWithContext(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(insets),
                    status = state.status,
                    context = state.context,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick,
                    onStatusAction = { action ->
                        component.onStatusAction(action)
                    },
                    onStatusReplyClick = onStatusReplyClick,
                    onAccountClick = onAccountClick
                )
            }
        }
    }
}
