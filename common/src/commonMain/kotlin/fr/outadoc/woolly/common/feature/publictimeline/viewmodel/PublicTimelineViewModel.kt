package fr.outadoc.woolly.common.feature.publictimeline.viewmodel

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PublicTimelineViewModel(
    pagingConfig: PagingConfig,
    viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider,
    statusActionRepository: StatusActionRepository
) {
    private val localPagingRepository = StatusPagingRepository(
        pagingConfig,
        clientProvider,
        statusActionRepository
    ) { client ->
        client.timelines.getPublicTimelineSource(onlyLocal = true)
    }

    private val globalPagingRepository = StatusPagingRepository(
        pagingConfig,
        clientProvider,
        statusActionRepository
    ) { client ->
        client.timelines.getPublicTimelineSource()
    }

    val localPagingItems: Flow<PagingData<Status>> =
        localPagingRepository
            .pagingData
            .cachedIn(viewModelScope)

    val globalPagingItems: Flow<PagingData<Status>> =
        globalPagingRepository
            .pagingData
            .cachedIn(viewModelScope)

    fun onLocalStatusAction(action: StatusAction) {
        localPagingRepository.onStatusAction(action)
    }

    fun onGlobalStatusAction(action: StatusAction) {
        globalPagingRepository.onStatusAction(action)
    }
}
