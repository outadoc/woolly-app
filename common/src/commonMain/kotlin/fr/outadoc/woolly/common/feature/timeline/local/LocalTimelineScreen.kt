package fr.outadoc.woolly.common.feature.timeline.local

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun LocalTimelineScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val viewModel: LocalTimelineViewModel by di.instance()
    val currentState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onPageOpen(Color(0xff64B5F6))
    }

    Timeline(insets = insets, state = currentState.value)
}
