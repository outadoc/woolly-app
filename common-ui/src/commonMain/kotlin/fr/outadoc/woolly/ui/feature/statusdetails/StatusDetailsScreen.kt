package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.statusdetails.viewmodel.StatusDetailsViewModel
import fr.outadoc.woolly.ui.feature.status.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.Status
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun StatusDetailsScreen(
    statusId: String,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<StatusDetailsViewModel>()
    val state by vm.state.collectAsState(StatusDetailsViewModel.State.Loading)

    LaunchedEffect(statusId) {
        vm.loadStatus(statusId)
    }

    when (val state = state) {
        StatusDetailsViewModel.State.Error -> {
            ErrorScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        StatusDetailsViewModel.State.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is StatusDetailsViewModel.State.LoadedStatus -> {
            StatusWithContext(
                modifier = Modifier.fillMaxSize(),
                status = state.status,
                context = state.context,
                onStatusClick = onStatusClick,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}

@Composable
fun StatusWithContext(
    modifier: Modifier = Modifier,
    status: Status,
    context: Context,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = LazyListState(
            firstVisibleItemIndex = context.ancestors.size
        )
    ) {
        if (context.ancestors.isNotEmpty()) {
            items(context.ancestors, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onStatusClick(status) },
                    status = status,
                    onAttachmentClick = onAttachmentClick
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
                onStatusAction = { TODO("call VM") }
            )
        }

        if (context.descendants.isNotEmpty()) {
            item {
                TabRowDefaults.Divider(thickness = 1.dp)
            }

            items(context.descendants, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onStatusClick(status) },
                    status = status,
                    onAttachmentClick = onAttachmentClick
                )
            }
        }
    }
}
