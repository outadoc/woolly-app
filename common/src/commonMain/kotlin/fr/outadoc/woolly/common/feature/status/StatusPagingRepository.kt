package fr.outadoc.woolly.common.feature.status

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn

class StatusPagingRepository(
    private val clientProvider: MastodonClientProvider,
    private val pagingSourceFactory: (MastodonClient) -> PagingSource<PageInfo, Status>
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
            .let(pagingSourceFactory)
            .also { newSource ->
                _latestPagingSource = newSource
            }

    private val _actionFlow = MutableSharedFlow<StatusAction>()
    val actionObserver: Flow<*> =
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

            invalidate()
        }.flowOn(Dispatchers.IO)

    val pagingData: Flow<PagingData<Status>> =
        Pager(pagingConfig) { pagingSource }.flow

    suspend fun onStatusAction(action: StatusAction) {
        _actionFlow.emit(action)
    }

    fun invalidate() {
        _latestPagingSource?.invalidate()
    }
}