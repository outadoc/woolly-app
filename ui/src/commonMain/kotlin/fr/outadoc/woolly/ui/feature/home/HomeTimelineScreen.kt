package fr.outadoc.woolly.ui.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun HomeTimelineScreen(
    component: HomeTimelineComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    StatusList(
        insets = insets,
        statusFlow = component.homePagingItems,
        lazyListState = component.listState,
        onStatusAction = component::onStatusAction,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick,
        onStatusReplyClick = onStatusReplyClick,
        onAccountClick = onAccountClick
    )
}
