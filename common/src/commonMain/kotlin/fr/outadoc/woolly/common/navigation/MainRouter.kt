package fr.outadoc.woolly.common.navigation

import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.global.GlobalTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.home.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.local.LocalTimelineScreen
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

    @Composable
    fun Drawer(
        currentScreen: AppScreen,
        drawerState: DrawerState?
    ) {
        MainAppDrawer(
            drawerState = drawerState,
            colorScheme = colorScheme,
            onColorSchemeChanged = onColorSchemeChanged,
            currentScreen = currentScreen,
            onScreenSelected = onScreenSelected
        )
    }

    when (currentScreen) {
        AppScreen.HomeTimeline -> HomeTimelineScreen(
            drawer = { drawerState -> Drawer(currentScreen, drawerState) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )

        AppScreen.GlobalTimeline -> GlobalTimelineScreen(
            drawer = { drawerState -> Drawer(currentScreen, drawerState) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )

        AppScreen.LocalTimeline -> LocalTimelineScreen(
            drawer = { drawerState -> Drawer(currentScreen, drawerState) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )

        AppScreen.Search -> SearchScreen(
            drawer = { drawerState -> Drawer(currentScreen, drawerState) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )
    }
}
