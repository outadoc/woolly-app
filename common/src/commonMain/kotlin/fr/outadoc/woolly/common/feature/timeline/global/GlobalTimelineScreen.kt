package fr.outadoc.woolly.common.feature.timeline.global

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.repository.StatusRepository
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun GlobalTimelineScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()

    Timeline(
        insets = insets,
        pagingSourceFactory = repo::getPublicGlobalTimelineSource
    )
}
