package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import fr.outadoc.woolly.common.ui.Timeline

@Composable
fun SearchScreen(
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    currentSubScreen: SearchSubScreen,
    onCurrentSubScreenChanged: (SearchSubScreen) -> Unit,
    statusPagingItems: LazyPagingItems<Status>,
    statusListState: LazyListState,
    accountsPagingItems: LazyPagingItems<Account>,
    accountsListState: LazyListState,
    hashtagsPagingItems: LazyPagingItems<Tag>,
    hashtagsListState: LazyListState,
    drawer: @Composable ColumnScope.(DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            SearchTopAppBar(
                searchTerm = searchTerm,
                onSearchTermChanged = onSearchTermChanged,
                drawerState = drawerState,
                currentSubScreen = currentSubScreen,
                onCurrentSubScreenChanged = onCurrentSubScreenChanged
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawerState -> drawer(drawerState) }
    ) { insets ->
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
}
