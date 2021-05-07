package fr.outadoc.mastodonk.common.feature.globaltimeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import fr.outadoc.mastodonk.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun GlobalTimelineScreen() {
    val di = LocalDI.current
    val viewModel: GlobalTimelineViewModel by di.instance()
    val currentState = viewModel.state.collectAsState()

    Timeline(state = currentState.value)
}
