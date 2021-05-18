package fr.outadoc.woolly.common.feature.client

import fr.outadoc.mastodonk.client.MastodonClient

val MastodonClientProvider.latestClientOrThrow: MastodonClient
    get() = mastodonClient.value
        ?: throw IllegalStateException(
            "Tried to use a Mastodon client but no one is logged in"
        )
