package fr.outadoc.woolly.common.navigation

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.global.GlobalTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.local.LocalTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.ui.ColorScheme

@Composable
fun MainRouter(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    var currentScreen: AppScreen by remember { mutableStateOf(AppScreen.GlobalTimeline) }

    val onScreenSelected = { screen: AppScreen ->
        currentScreen = screen
    }

    when (currentScreen) {
        AppScreen.GlobalTimeline -> GlobalTimelineScreen(
            scaffoldState = scaffoldState,
            drawer = { MainAppDrawer(colorScheme, onColorSchemeChanged, currentScreen, onScreenSelected) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )
        AppScreen.LocalTimeline -> LocalTimelineScreen(
            scaffoldState = scaffoldState,
            drawer = { MainAppDrawer(colorScheme, onColorSchemeChanged, currentScreen, onScreenSelected) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )
        AppScreen.Search -> SearchScreen(
            scaffoldState = scaffoldState,
            drawer = { MainAppDrawer(colorScheme, onColorSchemeChanged, currentScreen, onScreenSelected) },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )
    }
}
