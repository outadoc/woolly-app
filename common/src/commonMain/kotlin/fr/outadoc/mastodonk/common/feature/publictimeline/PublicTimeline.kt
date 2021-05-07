package fr.outadoc.mastodonk.common.feature.publictimeline

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import fr.outadoc.mastodonk.common.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun PublicTimelineScreen() {
    val di = LocalDI.current
    val viewModel: TimelineViewModel by di.instance()
    val currentState = viewModel.state.collectAsState()

    Timeline(state = currentState.value)
}
