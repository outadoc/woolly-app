package fr.outadoc.woolly.common.feature.publictimeline.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class PublicTimelineViewModel(
    viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    val localPagingItems: Flow<PagingData<Status>> =
        clientProvider
            .mastodonClient
            .filterNotNull()
            .flatMapLatest { client ->
                Pager(pagingConfig) {
                    client.timelines.getPublicTimelineSource(onlyLocal = true)
                }.flow
            }.cachedIn(viewModelScope)

    val globalPagingItems: Flow<PagingData<Status>> =
        clientProvider
            .mastodonClient
            .filterNotNull()
            .flatMapLatest { client ->
                Pager(pagingConfig) {
                    client.timelines.getPublicTimelineSource()
                }.flow
            }.cachedIn(viewModelScope)
}
