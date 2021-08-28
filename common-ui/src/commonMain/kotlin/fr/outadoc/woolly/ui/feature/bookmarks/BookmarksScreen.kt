package fr.outadoc.woolly.ui.feature.bookmarks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.bookmarks.viewmodel.BookmarksViewModel
import fr.outadoc.woolly.ui.feature.status.StatusList
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun BookmarksScreen(
    insets: PaddingValues,
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<BookmarksViewModel>()

    StatusList(
        insets = insets,
        statusFlow = vm.bookmarksPagingItems,
        lazyListState = listState,
        onStatusAction = vm::onStatusAction,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick
    )
}
