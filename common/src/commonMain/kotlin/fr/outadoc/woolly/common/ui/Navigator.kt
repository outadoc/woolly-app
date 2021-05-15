package fr.outadoc.woolly.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.global.GlobalTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.local.LocalTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen

@Composable
fun Navigator(toggleDarkMode: () -> Unit) {
    var currentScreen: AppScreen by remember { mutableStateOf(AppScreen.GlobalTimeline) }
    val onScreenSelected = { screen: AppScreen ->
        currentScreen = screen
    }

    when (currentScreen) {
        AppScreen.GlobalTimeline -> GlobalTimelineScreen(toggleDarkMode, onScreenSelected)
        AppScreen.LocalTimeline -> LocalTimelineScreen(toggleDarkMode, onScreenSelected)
        AppScreen.Search -> SearchScreen(onScreenSelected)
    }
}