package fr.outadoc.woolly.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.timeline.AnnotateStatusUseCase
import fr.outadoc.woolly.common.feature.timeline.global.GlobalTimelineViewModel
import fr.outadoc.woolly.common.feature.timeline.local.LocalTimelineViewModel
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.AppTheme
import fr.outadoc.woolly.common.ui.Navigator
import fr.outadoc.woolly.htmltext.HtmlParser
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI
import org.kodein.di.instance

private val di = DI {
    bindSingleton {
        MastodonClient {
            baseUrl = "https://mastodon.social"
        }
    }

    bindSingleton { AppScreenResources() }

    bindSingleton { HtmlParser() }
    bindSingleton { AnnotateStatusUseCase(instance()) }

    bindSingleton { GlobalTimelineViewModel(instance(), instance()) }
    bindSingleton { LocalTimelineViewModel(instance(), instance()) }
}

@Composable
fun App() = withDI(di) {
    var isDarkModeEnabled by remember { mutableStateOf(true) }
    var currentScreen: AppScreen by remember { mutableStateOf(AppScreen.GlobalTimeline) }

    AppTheme(isDarkModeEnabled = isDarkModeEnabled) {
        Navigator(
            currentScreen = currentScreen,
            toggleDarkMode = {
                isDarkModeEnabled = !isDarkModeEnabled
            },
            onScreenSelected = { screen ->
                currentScreen = screen
            }
        )
    }
}
