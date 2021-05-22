package fr.outadoc.woolly.common.feature.home.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeTimelineViewModel(
    private val viewModelScope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) {
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    private var _latestPagingSource: PagingSource<PageInfo, Status>? = null
    private val pagingSource: PagingSource<PageInfo, Status>
        get() = clientProvider
            .latestClientOrThrow
            .timelines
            .getHomeTimelineSource()
            .also { newSource ->
                _latestPagingSource = newSource
            }

    private val _actionFlow = MutableSharedFlow<StatusAction>()
    private val _actionObserver =
        combine(
            clientProvider.mastodonClient.filterNotNull(),
            _actionFlow
        ) { client, action ->
            with(client.statuses) {
                when (action) {
                    is StatusAction.Favourite -> favourite(action.status.statusId)
                    is StatusAction.UndoFavourite -> undoFavourite(action.status.statusId)
                    is StatusAction.Boost -> boost(action.status.statusId)
                    is StatusAction.UndoBoost -> undoBoost(action.status.statusId)
                    is StatusAction.Bookmark -> bookmark(action.status.statusId)
                    is StatusAction.UndoBookmark -> undoBookmark(action.status.statusId)
                }
            }

            _latestPagingSource?.invalidate()
        }.flowOn(Dispatchers.IO)

    val homePagingItems: Flow<PagingData<Status>> =
        Pager(pagingConfig) { pagingSource }
            .flow
            .cachedIn(viewModelScope)

    fun onStatusAction(statusAction: StatusAction) {
        viewModelScope.launch {
            _actionFlow.emit(statusAction)
        }
    }

    init {
        viewModelScope.launch {
            _actionObserver.collect()
        }
    }
}
