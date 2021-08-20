package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.statusdetails.viewmodel.StatusDetailsViewModel
import fr.outadoc.woolly.ui.feature.status.ErrorScreen
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
            StatusDetails(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                statusOrBoost = state.status,
                onStatusAction = { TODO("call VM") }
            )
        }
    }
}
