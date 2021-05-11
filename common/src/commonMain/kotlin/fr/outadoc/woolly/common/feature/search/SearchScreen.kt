package fr.outadoc.woolly.common.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.ui.MainBottomNavigation
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun SearchScreen(onScreenSelected: (AppScreen) -> Unit) {
    val di = LocalDI.current
    val searchRes by di.instance<SearchScreenResources>()

    var searchTerm by remember { mutableStateOf("") }
    var currentSubScreen by remember {
        mutableStateOf<SubSearchScreen>(SubSearchScreen.Statuses)
    }

    val currentScreen = AppScreen.Search
    val tabs = listOf(
        SubSearchScreen.Statuses,
        SubSearchScreen.Accounts,
        SubSearchScreen.Hashtags,
    )

    Scaffold(
        topBar = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = searchTerm,
                    onValueChange = { term -> searchTerm = term },
                    leadingIcon = {
                        Icon(Icons.Default.Search, "Search")
                    },
                    placeholder = {
                        Text("Search for something…")
                    },
                    singleLine = true,
                    trailingIcon = {
                        if (searchTerm.isNotEmpty()) {
                            IconButton(onClick = { searchTerm = "" }) {
                                Icon(Icons.Default.Clear, "Clear search")
                            }
                        }
                    }
                )

                if (searchTerm.isNotEmpty()) {
                    TabRow(selectedTabIndex = tabs.indexOf(currentSubScreen)) {
                        tabs.forEach { screen ->
                            Tab(
                                modifier = Modifier.height(48.dp),
                                selected = currentSubScreen == screen,
                                onClick = {
                                    currentSubScreen = screen
                                }
                            ) {
                                Text(text = searchRes.getScreenTitle(screen))
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            MainBottomNavigation(currentScreen, onScreenSelected)
        }
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
