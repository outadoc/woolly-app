package fr.outadoc.woolly.common.feature.home.viewmodel

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class HomeTimelineViewModel(
    viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider,
    pagingConfig: PagingConfig,
    statusActionRepository: StatusActionRepository
) {
    private val pagingRepository = StatusPagingRepository(
        pagingConfig,
        clientProvider,
        statusActionRepository
    ) { client ->
        client.timelines.getHomeTimelineSource()
    }

    val homePagingItems: Flow<PagingData<Status>> =
        pagingRepository
            .pagingData
            .cachedIn(viewModelScope)

    fun onStatusAction(action: StatusAction) {
        pagingRepository.onStatusAction(action)
    }
}
