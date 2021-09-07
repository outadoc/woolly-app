package fr.outadoc.woolly.ui.feature.favourites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.favourites.component.FavouritesComponent
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun FavouritesScreen(
    component: FavouritesComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {}
) {
    StatusList(
        insets = insets,
        statusFlow = component.favouritesPagingItems,
        lazyListState = component.listState,
        onStatusAction = component::onStatusAction,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick,
        onStatusReplyClick = onStatusReplyClick
    )
}
