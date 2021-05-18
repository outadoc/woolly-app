package fr.outadoc.woolly.common.feature.client

import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.flow.StateFlow

interface MastodonClientProvider {
    val mastodonClient: StateFlow<MastodonClient?>
}
