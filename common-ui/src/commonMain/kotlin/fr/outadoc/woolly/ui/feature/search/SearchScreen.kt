package fr.outadoc.woolly.ui.feature.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.ui.feature.account.AccountList
import fr.outadoc.woolly.ui.feature.status.StatusList
import fr.outadoc.woolly.ui.feature.tags.TagList
import fr.outadoc.woolly.ui.feature.tags.TrendingScreen

@Composable
fun SearchScreen(
    component: SearchComponent,
    insets: PaddingValues = PaddingValues(),
    currentSubScreen: SearchSubScreen,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {}
) {
    val state by component.state.collectAsState()

    if (state.query.isEmpty()) {
        TrendingScreen(
            trendingTags = component.trendingTags,
            insets = insets
        )
    } else {
        when (currentSubScreen) {
            SearchSubScreen.Statuses -> StatusList(
                insets = insets,
                statusFlow = component.statusPagingItems,
                lazyListState = component.statusListState,
                onStatusAction = component::onStatusAction,
                onStatusClick = onStatusClick,
                onAttachmentClick = onAttachmentClick,
                onStatusReplyClick = onStatusReplyClick
            )

            SearchSubScreen.Accounts -> AccountList(
                insets = insets,
                accountFlow = component.accountsPagingItems,
                lazyListState = component.accountsListState
            )

            SearchSubScreen.Hashtags -> TagList(
                insets = insets,
                tagFlow = component.hashtagsPagingItems,
                lazyListState = component.hashtagsListState
            )
        }
    }
}
