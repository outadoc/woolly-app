package fr.outadoc.woolly.common.feature.home.viewmodel

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeTimelineViewModel(
    private val viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider,
    pagingConfig: PagingConfig
) {
    private val pagingRepository: StatusPagingRepository =
        StatusPagingRepository(pagingConfig, clientProvider) { client ->
            client.timelines.getHomeTimelineSource()
        }

    val homePagingItems: Flow<PagingData<Status>> =
        pagingRepository
            .pagingData
            .cachedIn(viewModelScope)

    fun onStatusAction(action: StatusAction) {
        viewModelScope.launch {
            pagingRepository.onStatusAction(action)
        }
    }

    init {
        viewModelScope.launch {
            pagingRepository.actionObserver.collect()
        }
    }
}
