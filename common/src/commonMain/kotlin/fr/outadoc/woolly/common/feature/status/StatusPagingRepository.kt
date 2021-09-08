package fr.outadoc.woolly.common.feature.status

import androidx.paging.*
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import kotlinx.coroutines.CoroutineScope
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

    fun getPagingData(
        componentScope: CoroutineScope,
        factory: (MastodonClient) -> PagingSource<PageInfo, Status>
    ): Flow<PagingData<Status>> =
        Pager(pagingConfig) { getPagingSource(factory) }
            .flow
            .cachedIn(componentScope)
            .combine(statusActionRepository.cachedStatusDeltas) { data, deltas ->
                data.map { status ->
                    when (val statusDelta = deltas[status.statusId]) {
                        null -> status
                        else -> status.copy(
                            isBoosted = statusDelta.isBoosted ?: status.isBoosted,
                            isFavourited = statusDelta.isFavourited ?: status.isFavourited,
                            isBookmarked = statusDelta.isBookmarked ?: status.isBookmarked
                        )
                    }
                }
            }

    fun invalidateSource() {
        _latestPagingSource?.invalidate()
    }
}