package fr.outadoc.woolly.common.feature.statusdetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.ui.ErrorScreen
import fr.outadoc.woolly.common.feature.status.ui.StatusOrBoost
import fr.outadoc.woolly.common.feature.statusdetails.ui.viewmodel.StatusDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun StatusDetailsScreen(statusId: String) {
    val di = LocalDI.current
    val vm by di.instance<StatusDetailsViewModel>()
    val state by vm.state.collectAsState(StatusDetailsViewModel.State.Loading)

    // Periodically refresh timestamps
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    rememberCoroutineScope().launch(Dispatchers.Default) {
        while (isActive) {
            delay(1_000)
            currentTime = Clock.System.now()
        }
    }

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
            StatusDetails(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                status = state.status,
                currentTime = currentTime
            )
        }
    }
}

@Composable
fun StatusDetails(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
) {
    StatusOrBoost(
        modifier = modifier,
        status = status,
        currentTime = currentTime,
        onStatusAction = {}
    )
}
