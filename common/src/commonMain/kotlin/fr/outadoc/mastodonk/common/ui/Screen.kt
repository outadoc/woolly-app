package fr.outadoc.mastodonk.common.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.common.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.mastodonk.common.screen.AppScreen
import fr.outadoc.mastodonk.common.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Screen(
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
        content = {
            when (currentScreen) {
                AppScreen.PublicTimeline -> PublicTimelineScreen()
                AppScreen.LocalTimeline -> TODO()
            }
        },
        bottomBar = {
            MainBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = onScreenSelected
            )
        }
    )
}
