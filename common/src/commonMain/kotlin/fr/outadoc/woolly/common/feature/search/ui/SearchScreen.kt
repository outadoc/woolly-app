package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.ui.Timeline

@Composable
fun SearchScreen(
    insets: PaddingValues,
    searchTerm: String,
    currentSubScreen: SearchSubScreen,
    statusPagingItems: LazyPagingItems<Status>,
    statusListState: LazyListState,
    accountsPagingItems: LazyPagingItems<Account>,
    accountsListState: LazyListState,
    hashtagsPagingItems: LazyPagingItems<Tag>,
    hashtagsListState: LazyListState
) {
    if (searchTerm.isEmpty()) {
        TrendingScreen()
    } else {
        when (currentSubScreen) {
            is SearchSubScreen.Statuses -> Timeline(
                insets = insets,
                lazyPagingItems = statusPagingItems,
                lazyListState = statusListState
            )
            is SearchSubScreen.Accounts -> Unit
            is SearchSubScreen.Hashtags -> Unit
        }
    }
}
