package fr.outadoc.woolly.common.feature.timeline.repository

import androidx.paging.PagingSource
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow

class StatusRepository(private val clientProvider: MastodonClientProvider) {

    private val client get() = clientProvider.latestClientOrThrow

    fun getHomeTimelineSource(): PagingSource<PageInfo, Status> {
        return client.timelines.getHomeTimelineSource()
    }

    fun getPublicLocalTimelineSource(): PagingSource<PageInfo, Status> {
        return client.timelines.getPublicTimelineSource(onlyLocal = true)
    }

    fun getPublicGlobalTimelineSource(): PagingSource<PageInfo, Status> {
        return client.timelines.getPublicTimelineSource()
    }
}
