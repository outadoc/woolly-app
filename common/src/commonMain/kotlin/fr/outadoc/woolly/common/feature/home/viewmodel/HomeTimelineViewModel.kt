package fr.outadoc.woolly.common.feature.home.viewmodel

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
    clientProvider: MastodonClientProvider
) {
    private val pagingWrapper: StatusPagingRepository =
        StatusPagingRepository(clientProvider) { client ->
            client.timelines.getHomeTimelineSource()
        }

    val homePagingItems: Flow<PagingData<Status>> =
        pagingWrapper
            .pagingData
            .cachedIn(viewModelScope)

    fun onStatusAction(action: StatusAction) {
        viewModelScope.launch {
            pagingWrapper.onStatusAction(action)
        }
    }

    init {
        viewModelScope.launch {
            pagingWrapper.actionObserver.collect()
        }
    }
}
