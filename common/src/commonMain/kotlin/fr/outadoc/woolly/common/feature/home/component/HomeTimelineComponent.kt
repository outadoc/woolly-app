package fr.outadoc.woolly.common.feature.home.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.doOnDestroy
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHomeTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

class HomeTimelineComponent(
    componentContext: ComponentContext,
    clientProvider: MastodonClientProvider,
    pagingConfig: PagingConfig,
    statusActionRepository: StatusActionRepository
) : ComponentContext by componentContext {

    private val componentScope = MainScope()

    // TODO save state
    val listState = LazyListState()

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
            .cachedIn(componentScope)

    fun onStatusAction(action: StatusAction) {
        pagingRepository.onStatusAction(action)
    }

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }
}
