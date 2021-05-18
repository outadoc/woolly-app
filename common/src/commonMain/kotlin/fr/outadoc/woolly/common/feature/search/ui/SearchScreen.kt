package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.ui.ResponsiveScaffold

@Composable
fun SearchScreen(
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    currentSubScreen: SearchSubScreen,
    onCurrentSubScreenChanged: (SearchSubScreen) -> Unit,
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
                is SearchSubScreen.Statuses -> StatusSearchResults(searchTerm, insets)
                is SearchSubScreen.Accounts -> AccountSearchResults(searchTerm, insets)
                is SearchSubScreen.Hashtags -> HashtagSearchResults(searchTerm, insets)
            }
        }
    }
}
