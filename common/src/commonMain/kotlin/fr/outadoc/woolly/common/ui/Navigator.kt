package fr.outadoc.woolly.common.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.timeline.global.GlobalTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.local.LocalTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Navigator(
    currentScreen: AppScreen,
    toggleDarkMode: () -> Unit,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res: AppScreenResources by di.instance()

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = res.getScreenTitle(currentScreen),
                toggleDarkMode = toggleDarkMode
            )
        },
        bottomBar = {
            MainBottomNavigation(currentScreen, onScreenSelected)
        }
    ) { insets ->
        when (currentScreen) {
            AppScreen.GlobalTimeline -> GlobalTimelineScreen(insets)
            AppScreen.LocalTimeline -> LocalTimelineScreen(insets)
        }
    }
}
