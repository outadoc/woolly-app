package fr.outadoc.woolly.ui.feature.bookmarks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.bookmarks.viewmodel.BookmarksViewModel
import fr.outadoc.woolly.ui.feature.status.StatusList

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel,
    insets: PaddingValues = PaddingValues(),
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    StatusList(
        insets = insets,
        statusFlow = viewModel.bookmarksPagingItems,
        lazyListState = listState,
        onStatusAction = viewModel::onStatusAction,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick
    )
}
