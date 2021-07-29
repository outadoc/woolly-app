package fr.outadoc.woolly.common.feature.bookmarks.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.bookmarks.viewmodel.BookmarksViewModel
import fr.outadoc.woolly.common.feature.status.ui.StatusList
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun BookmarksScreen(
    insets: PaddingValues,
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<BookmarksViewModel>()

    StatusList(
        insets = insets,
        statusFlow = vm.bookmarksPagingItems,
        lazyListState = listState,
        onStatusAction = vm::onStatusAction,
        onStatusClick = onStatusClick
    )
}
