package fr.outadoc.mastodonk.common.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.common.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.mastodonk.common.screen.AppScreen
import fr.outadoc.mastodonk.common.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Screen(screen: AppScreen, toggleDarkMode: () -> Unit) {
    val di = LocalDI.current
    val res: AppScreenResources by di.instance()

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = res.getScreenTitle(screen),
                toggleDarkMode = toggleDarkMode
            )
        },
        content = {
            when (screen) {
                AppScreen.PublicTimeline -> PublicTimelineScreen()
                AppScreen.LocalTimeline -> TODO()
            }
        }
    )
}
