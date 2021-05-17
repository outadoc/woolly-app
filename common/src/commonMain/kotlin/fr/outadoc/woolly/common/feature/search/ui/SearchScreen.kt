package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.search.SubSearchScreen
import fr.outadoc.woolly.common.ui.ResponsiveScaffold

@Composable
fun SearchScreen(
    drawer: @Composable ColumnScope.(DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var searchTerm by remember { mutableStateOf("") }
    var currentSubScreen by remember {
        mutableStateOf<SubSearchScreen>(SubSearchScreen.Statuses)
    }

    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            SearchTopAppBar(
                searchTerm = searchTerm,
                onSearchTermChanged = { searchTerm = it },
                drawerState = drawerState,
                currentSubScreen = currentSubScreen,
                onCurrentSubScreenChanged = { currentSubScreen = it }
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawerState -> drawer(drawerState) }
    ) { insets ->
        if (searchTerm.isEmpty()) {
            TrendingScreen()
        } else {
            when (currentSubScreen) {
                is SubSearchScreen.Statuses -> StatusSearchResults(searchTerm, insets)
                is SubSearchScreen.Accounts -> AccountSearchResults(searchTerm, insets)
                is SubSearchScreen.Hashtags -> HashtagSearchResults(searchTerm, insets)
            }
        }
    }
}
