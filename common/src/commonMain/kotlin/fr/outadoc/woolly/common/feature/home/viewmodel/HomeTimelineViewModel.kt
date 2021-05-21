package fr.outadoc.woolly.common.feature.home.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class HomeTimelineViewModel(
    viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    val homePagingItems: Flow<PagingData<Status>> =
        clientProvider
            .mastodonClient
            .filterNotNull()
            .flatMapLatest { client ->
                Pager(pagingConfig) {
                    client.timelines.getHomeTimelineSource()
                }.flow
            }.cachedIn(viewModelScope)

}
