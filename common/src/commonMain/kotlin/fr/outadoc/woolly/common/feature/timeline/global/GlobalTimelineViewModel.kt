package fr.outadoc.woolly.common.feature.timeline.global

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource

class GlobalTimelineViewModel(mastodonClient: MastodonClient) {

    val pagingSource = mastodonClient.timelines.getPublicTimelineSource()
}
