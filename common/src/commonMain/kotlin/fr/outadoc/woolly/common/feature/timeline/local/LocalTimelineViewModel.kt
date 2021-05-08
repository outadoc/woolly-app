package fr.outadoc.woolly.common.feature.timeline.local

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource

class LocalTimelineViewModel(mastodonClient: MastodonClient) {

    val pagingSource = mastodonClient.timelines.getPublicTimelineSource(onlyLocal = true)
}
