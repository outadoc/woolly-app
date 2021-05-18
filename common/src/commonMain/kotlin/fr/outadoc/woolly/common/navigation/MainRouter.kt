package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.ui.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.ui.PublicTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.ui.ColorScheme

@Composable
fun MainRouter(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    var currentScreen: AppScreen by remember { mutableStateOf(AppScreen.HomeTimeline) }

    val onScreenSelected = { screen: AppScreen ->
        currentScreen = screen
    }

    when (currentScreen) {
        AppScreen.HomeTimeline -> HomeTimelineScreen(
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

        AppScreen.PublicTimeline -> PublicTimelineScreen(
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

        AppScreen.Search -> SearchScreen(
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

        AppScreen.Account -> AccountScreen(
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
