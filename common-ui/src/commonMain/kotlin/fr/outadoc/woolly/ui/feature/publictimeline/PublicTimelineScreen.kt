package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun PublicTimelineScreen(
    component: PublicTimelineComponent,
    insets: PaddingValues = PaddingValues(),
    currentSubScreen: PublicTimelineSubScreen,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    when (currentSubScreen) {
        PublicTimelineSubScreen.Local -> StatusList(
            insets = insets,
            statusFlow = component.localPagingItems,
            lazyListState = component.localListState,
            onStatusAction = component::onLocalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick
        )
        PublicTimelineSubScreen.Global -> StatusList(
            insets = insets,
            statusFlow = component.globalPagingItems,
            lazyListState = component.globalListState,
            onStatusAction = component::onGlobalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick
        )
    }
}
