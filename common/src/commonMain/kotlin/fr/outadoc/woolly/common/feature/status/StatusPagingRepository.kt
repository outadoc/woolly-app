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
import kotlinx.coroutines.flow.Flow

class StatusPagingRepository(
    pagingConfig: PagingConfig,
    private val clientProvider: MastodonClientProvider,
    private val statusActionRepository: StatusActionRepository,
    private val pagingSourceFactory: (MastodonClient) -> PagingSource<PageInfo, Status>
) {
    private var _latestPagingSource: PagingSource<PageInfo, Status>? = null
    private val pagingSource: PagingSource<PageInfo, Status>
        get() = clientProvider
            .latestClientOrThrow
            .let(pagingSourceFactory)
            .also { newSource ->
                _latestPagingSource = newSource
            }

    init {
        statusActionRepository.addOnActionPerformedListener {
            invalidate()
        }
    }

    val pagingData: Flow<PagingData<Status>> =
        Pager(pagingConfig) { pagingSource }.flow

    fun onStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    fun invalidate() {
        _latestPagingSource?.invalidate()
    }
}