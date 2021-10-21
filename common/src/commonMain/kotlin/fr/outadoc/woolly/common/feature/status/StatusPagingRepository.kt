package fr.outadoc.woolly.common.feature.status

import androidx.paging.*
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class StatusPagingRepository(
    private val pagingConfig: PagingConfig,
    private val clientProvider: MastodonClientProvider,
    private val statusActionRepository: StatusActionRepository
) {
    private var _latestPagingSource: PagingSource<PageInfo, Status>? = null

    private fun getPagingSource(
        factory: (MastodonClient) -> PagingSource<PageInfo, Status>
    ): PagingSource<PageInfo, Status> {
        return clientProvider
            .latestClientOrThrow
            .let(factory)
            .also { newSource ->
                _latestPagingSource = newSource
            }
    }

    fun getPagingData(factory: (MastodonClient) -> PagingSource<PageInfo, Status>): Flow<PagingData<Status>> =
        Pager(pagingConfig) { getPagingSource(factory) }
            .flow
            .combine(statusActionRepository.cachedStatusDeltas) { data, deltas ->
                data.map { status ->
                    // Apply status action deltas to the statuses in this list.
                    // If this status is a boost, update the underlying boosted status with the cached action.
                    // Either way, update the actual status with the associated cached action.
                    when (val boostedStatus = status.boostedStatus) {
                        null -> status
                        else -> status.copy(boostedStatus = boostedStatus + deltas[boostedStatus.statusId])
                    } + deltas[status.statusId]
                }
            }

    fun invalidateSource() {
        _latestPagingSource?.invalidate()
    }
}