package fr.outadoc.woolly.ui.feature.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.viewmodel.SearchViewModel
import fr.outadoc.woolly.ui.feature.account.AccountList
import fr.outadoc.woolly.ui.feature.status.StatusList
import fr.outadoc.woolly.ui.feature.tags.TagList
import fr.outadoc.woolly.ui.feature.tags.TrendingScreen

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    insets: PaddingValues = PaddingValues(),
    currentSubScreen: SearchSubScreen,
    statusListState: LazyListState,
    accountsListState: LazyListState,
    hashtagsListState: LazyListState,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    if (state.query.isEmpty()) {
        TrendingScreen(
            trendingTags = viewModel.trendingTags,
            insets = insets
        )
    } else {
        when (currentSubScreen) {
            SearchSubScreen.Statuses -> StatusList(
                insets = insets,
                statusFlow = viewModel.statusPagingItems,
                lazyListState = statusListState,
                onStatusAction = viewModel::onStatusAction,
                onStatusClick = onStatusClick,
                onAttachmentClick = onAttachmentClick
            )

            SearchSubScreen.Accounts -> AccountList(
                insets = insets,
                accountFlow = viewModel.accountsPagingItems,
                lazyListState = accountsListState
            )

            SearchSubScreen.Hashtags -> TagList(
                insets = insets,
                tagFlow = viewModel.hashtagsPagingItems,
                lazyListState = hashtagsListState
            )
        }
    }
}
