package fr.outadoc.mastodonk.common

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.GlobalScope

val mastodonClient = MastodonClient {
    baseUrl = "https://mastodon.social"
}

val timelineViewModel = TimelineViewModel(
    scope = GlobalScope,
    mastodonClient = mastodonClient
)

@Composable
fun AppTheme(
    isDarkModeEnabled: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkModeEnabled) darkColors() else lightColors(),
        content = content
    )
}

@Composable
fun App() {
    var isDarkModeEnabled by remember { mutableStateOf(false) }
    AppTheme(isDarkModeEnabled = isDarkModeEnabled) {
        TimelineScreen(timelineViewModel, toggleDarkMode = {
            isDarkModeEnabled = !isDarkModeEnabled
        })
    }
}
