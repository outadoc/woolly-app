package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.ui.feature.status.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.Status

@Composable
fun StatusDetailsScreen(
    component: StatusDetailsComponent,
    statusId: String,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
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
                    }
                )
            }
        }
    }
}

@Composable
fun StatusWithContext(
    modifier: Modifier = Modifier,
    status: Status,
    context: Context,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusAction: (StatusAction) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(
            initialFirstVisibleItemIndex = context.ancestors.size
        )
    ) {
        if (context.ancestors.isNotEmpty()) {
            items(context.ancestors, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier
                        .clickable { onStatusClick(status) }
                        .padding(16.dp),
                    status = status,
                    onAttachmentClick = onAttachmentClick,
                    onStatusAction = onStatusAction
                )
            }

            item {
                TabRowDefaults.Divider(thickness = 1.dp)
            }
        }

        item {
            StatusDetails(
                modifier = Modifier.padding(16.dp),
                statusOrBoost = status,
                onAttachmentClick = onAttachmentClick,
                onStatusAction = onStatusAction
            )
        }

        if (context.descendants.isNotEmpty()) {
            item {
                TabRowDefaults.Divider(thickness = 1.dp)
            }

            items(context.descendants, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier
                        .clickable { onStatusClick(status) }
                        .padding(16.dp),
                    status = status,
                    onAttachmentClick = onAttachmentClick,
                    onStatusAction = onStatusAction
                )
            }
        }
    }
}
