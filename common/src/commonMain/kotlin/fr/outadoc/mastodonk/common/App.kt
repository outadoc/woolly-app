package fr.outadoc.mastodonk.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.mastodonk.common.screen.AppScreen
import fr.outadoc.mastodonk.common.screen.AppScreenResources
import fr.outadoc.mastodonk.common.feature.publictimeline.TimelineViewModel
import kotlinx.coroutines.GlobalScope
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI
import org.kodein.di.instance

val di = DI {
    bindSingleton {
        MastodonClient {
            baseUrl = "https://mastodon.social"
        }
    }

    bindSingleton { AppScreenResources() }

    bindSingleton {
        TimelineViewModel(
            scope = GlobalScope,
            mastodonClient = instance()
        )
    }
}

@Composable
fun App() = withDI(di) {
    var isDarkModeEnabled by remember { mutableStateOf(true) }
    AppTheme(isDarkModeEnabled = isDarkModeEnabled) {
        Screen(
            screen = AppScreen.PublicTimeline,
            toggleDarkMode = {
                isDarkModeEnabled = !isDarkModeEnabled
            }
        )
    }
}
