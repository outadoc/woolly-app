package fr.outadoc.mastodonk.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
fun App() {
    MaterialTheme {
        TimelineScreen(timelineViewModel)
    }
}
