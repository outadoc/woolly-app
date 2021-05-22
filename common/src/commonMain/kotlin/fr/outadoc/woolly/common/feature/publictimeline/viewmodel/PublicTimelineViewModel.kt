package fr.outadoc.woolly.common.feature.publictimeline.viewmodel

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PublicTimelineViewModel(
    pagingConfig: PagingConfig,
    private val viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    private val localPagingRepository: StatusPagingRepository =
        StatusPagingRepository(pagingConfig, clientProvider) { client ->
            client.timelines.getPublicTimelineSource(onlyLocal = true)
        }

    private val globalPagingRepository: StatusPagingRepository =
        StatusPagingRepository(pagingConfig, clientProvider) { client ->
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
        viewModelScope.launch {
            localPagingRepository.onStatusAction(action)
        }
    }

    fun onGlobalStatusAction(action: StatusAction) {
        viewModelScope.launch {
            globalPagingRepository.onStatusAction(action)
        }
    }

    init {
        viewModelScope.launch {
            localPagingRepository.actionObserver.collect()
            globalPagingRepository.actionObserver.collect()
        }
    }
}
