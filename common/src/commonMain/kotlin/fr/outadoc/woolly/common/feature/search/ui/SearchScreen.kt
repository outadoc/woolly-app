package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.search.SubSearchScreen

@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    drawer: @Composable ColumnScope.() -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var searchTerm by remember { mutableStateOf("") }
    var currentSubScreen by remember {
        mutableStateOf<SubSearchScreen>(SubSearchScreen.Statuses)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchTopAppBar(
                searchTerm = searchTerm,
                onSearchTermChanged = { searchTerm = it },
                scaffoldState = scaffoldState,
                currentSubScreen = currentSubScreen,
                onCurrentSubScreenChanged = { currentSubScreen = it }
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawer() }
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
