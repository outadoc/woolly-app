package fr.outadoc.woolly.common.feature.timeline.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.ui.Timeline

@Composable
fun HomeTimelineScreen(
    insets: PaddingValues,
    pagingItems: LazyPagingItems<Status>,
    listState: LazyListState
) {
    Timeline(
        insets = insets,
        lazyPagingItems = pagingItems,
        lazyListState = listState
    )
}
