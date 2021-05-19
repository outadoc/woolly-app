package fr.outadoc.woolly.common.feature.timeline.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.ui.Timeline

@Composable
fun PublicTimelineScreen(
    insets: PaddingValues,
    currentSubScreen: PublicTimelineSubScreen,
    localPagingItems: LazyPagingItems<Status>,
    localListState: LazyListState,
    globalPagingItems: LazyPagingItems<Status>,
    globalListState: LazyListState
) {
    when (currentSubScreen) {
        is PublicTimelineSubScreen.Local -> Timeline(
            insets = insets,
            lazyPagingItems = localPagingItems,
            lazyListState = localListState
        )
        is PublicTimelineSubScreen.Global -> Timeline(
            insets = insets,
            lazyPagingItems = globalPagingItems,
            lazyListState = globalListState
        )
    }
}
