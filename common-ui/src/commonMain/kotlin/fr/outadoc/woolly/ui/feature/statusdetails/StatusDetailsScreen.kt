package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.statusdetails.viewmodel.StatusDetailsViewModel
import fr.outadoc.woolly.ui.feature.status.ErrorScreen
import fr.outadoc.woolly.ui.feature.status.Status
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun StatusDetailsScreen(statusId: String) {
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
                status = state.status,
                context = state.context
            )
        }
    }
}

@Composable
fun StatusWithContext(
    modifier: Modifier = Modifier,
    status: Status,
    context: Context?
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        state = lazyListState
    ) {
        context?.ancestors?.let { ancestors ->
            items(ancestors, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier.padding(vertical = 16.dp),
                    status = status
                )
            }
        }

        item {
            StatusDetails(
                statusOrBoost = status,
                onStatusAction = { TODO("call VM") }
            )
        }

        context?.descendants?.let { descendants ->
            items(descendants, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier.padding(vertical = 16.dp),
                    status = status
                )
            }
        }
    }
}
