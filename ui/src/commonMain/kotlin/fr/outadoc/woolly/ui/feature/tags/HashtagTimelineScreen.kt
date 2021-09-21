package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.tags.component.HashtagTimelineComponent
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun HashtagTimelineScreen(
    component: HashtagTimelineComponent,
    hashtag: String,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    LaunchedEffect(hashtag) {
        component.loadHashtag(hashtag)
    }

    StatusList(
        insets = insets,
        statusFlow = component.hashtagPagingItems,
        lazyListState = component.listState,
        onStatusAction = component::onStatusAction,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick,
        onStatusReplyClick = onStatusReplyClick,
        onAccountClick = onAccountClick
    )
}
