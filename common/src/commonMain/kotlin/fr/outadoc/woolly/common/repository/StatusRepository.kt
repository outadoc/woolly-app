package fr.outadoc.woolly.common.repository

import androidx.paging.PagingSource
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource

class StatusRepository(private val client: MastodonClient) {

    fun getPublicLocalTimelineSource(): PagingSource<PageInfo, Status> {
        return client.timelines.getPublicTimelineSource(onlyLocal = true)
    }

    fun getPublicGlobalTimelineSource(): PagingSource<PageInfo, Status> {
        return client.timelines.getPublicTimelineSource()
    }
}