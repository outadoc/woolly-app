package fr.outadoc.woolly.common.feature.timeline.local

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.MainBottomNavigation
import fr.outadoc.woolly.common.ui.MainTopAppBar
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun LocalTimelineScreen(
    toggleDarkMode: () -> Unit,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()
    val res by di.instance<AppScreenResources>()

    val currentScreen = AppScreen.LocalTimeline

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
        Timeline(
            insets = insets,
            pagingSourceFactory = repo::getPublicLocalTimelineSource
        )
    }
}
