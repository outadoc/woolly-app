package fr.outadoc.mastodonk.common.feature.localtimeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import fr.outadoc.mastodonk.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun LocalTimelineScreen() {
    val di = LocalDI.current
    val viewModel: LocalTimelineViewModel by di.instance()
    val currentState = viewModel.state.collectAsState()

    Timeline(state = currentState.value)
}
