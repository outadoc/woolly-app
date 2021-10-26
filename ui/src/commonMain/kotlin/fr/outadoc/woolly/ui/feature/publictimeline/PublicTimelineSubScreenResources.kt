package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun PublicTimelineSubScreen.getTitle() = when (this) {
    PublicTimelineSubScreen.Local -> stringResource(MR.strings.publicTimeline_local_title)
    PublicTimelineSubScreen.Global -> stringResource(MR.strings.publicTimeline_global_title)
}
