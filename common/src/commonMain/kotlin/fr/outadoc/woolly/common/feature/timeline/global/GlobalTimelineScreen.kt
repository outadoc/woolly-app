package fr.outadoc.woolly.common.feature.timeline.global

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun GlobalTimelineScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val viewModel: GlobalTimelineViewModel by di.instance()
    val currentState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onPageOpen()
    }

    Timeline(insets = insets, state = currentState.value)
}
