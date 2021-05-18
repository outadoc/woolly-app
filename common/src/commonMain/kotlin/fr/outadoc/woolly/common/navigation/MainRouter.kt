package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.timeline.ui.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.ui.PublicTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.ui.ColorScheme

@Composable
fun MainRouter(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    var currentScreen: AppScreen by rememberSaveable {
        mutableStateOf(AppScreen.HomeTimeline)
    }
    val onScreenSelected = { screen: AppScreen -> currentScreen = screen }

    var searchTerm by remember { mutableStateOf("") }
    var currentSearchScreen: SearchSubScreen by rememberSaveable {
        mutableStateOf(SearchSubScreen.Statuses)
    }

    var currentPublicTimelineScreen: PublicTimelineSubScreen by rememberSaveable {
        mutableStateOf(PublicTimelineSubScreen.Local)
    }

    when (currentScreen) {
        AppScreen.HomeTimeline -> key("home") {
            HomeTimelineScreen(
                drawer = { drawerState ->
                    MainAppDrawer(
                        drawerState = drawerState,
                        colorScheme = colorScheme,
                        onColorSchemeChanged = onColorSchemeChanged,
                        currentScreen = currentScreen,
                        onScreenSelected = onScreenSelected
                    )
                },
                bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
            )
        }

        AppScreen.PublicTimeline -> key("public") {
            PublicTimelineScreen(
                currentSubScreen = currentPublicTimelineScreen,
                onCurrentSubScreenChanged = { currentPublicTimelineScreen = it },
                drawer = { drawerState ->
                    MainAppDrawer(
                        drawerState = drawerState,
                        colorScheme = colorScheme,
                        onColorSchemeChanged = onColorSchemeChanged,
                        currentScreen = currentScreen,
                        onScreenSelected = onScreenSelected
                    )
                }
            ) { MainBottomNavigation(currentScreen, onScreenSelected) }
        }

        AppScreen.Search -> key("search") {
            SearchScreen(
                searchTerm = searchTerm,
                onSearchTermChanged = { searchTerm = it },
                currentSubScreen = currentSearchScreen,
                onCurrentSubScreenChanged = { currentSearchScreen = it },
                drawer = { drawerState ->
                    MainAppDrawer(
                        drawerState = drawerState,
                        colorScheme = colorScheme,
                        onColorSchemeChanged = onColorSchemeChanged,
                        currentScreen = currentScreen,
                        onScreenSelected = onScreenSelected
                    )
                },
                bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
            )
        }

        AppScreen.Account -> key("account") {
            AccountScreen(
                drawer = { drawerState ->
                    MainAppDrawer(
                        drawerState = drawerState,
                        colorScheme = colorScheme,
                        onColorSchemeChanged = onColorSchemeChanged,
                        currentScreen = currentScreen,
                        onScreenSelected = onScreenSelected
                    )
                },
                bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
            )
        }
    }
}
