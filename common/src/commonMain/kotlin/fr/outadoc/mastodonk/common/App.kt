package fr.outadoc.mastodonk.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.client.MastodonClient

@Composable
fun App() {
    val mastodonClient = MastodonClient {
        baseUrl = "https://mastodon.social"
    }

    MaterialTheme {
        Timeline(mastodonClient)
    }
}
