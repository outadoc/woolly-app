package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun PublicTimelineScreen(
    component: PublicTimelineComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val state by component.state.collectAsState()
    when (state.subScreen) {
        PublicTimelineSubScreen.Local -> StatusList(
            insets = insets,
            statusFlow = component.localPagingItems,
            lazyListState = component.localListState,
            onStatusAction = component::onLocalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        PublicTimelineSubScreen.Global -> StatusList(
            insets = insets,
            statusFlow = component.globalPagingItems,
            lazyListState = component.globalListState,
            onStatusAction = component::onGlobalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
    }
}
