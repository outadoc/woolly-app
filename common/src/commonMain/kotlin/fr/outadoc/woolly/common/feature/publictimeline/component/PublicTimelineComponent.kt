package fr.outadoc.woolly.common.feature.publictimeline.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.doOnDestroy
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

class PublicTimelineComponent(
    componentContext: ComponentContext,
    pagingConfig: PagingConfig,
    clientProvider: MastodonClientProvider,
    statusActionRepository: StatusActionRepository
) : ComponentContext by componentContext {

    private val componentScope = MainScope()

    // TODO save state
    val localListState = LazyListState()
    val globalListState = LazyListState()

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
            .cachedIn(componentScope)

    val globalPagingItems: Flow<PagingData<Status>> =
        globalPagingRepository
            .pagingData
            .cachedIn(componentScope)

    fun onLocalStatusAction(action: StatusAction) {
        localPagingRepository.onStatusAction(action)
    }

    fun onGlobalStatusAction(action: StatusAction) {
        globalPagingRepository.onStatusAction(action)
    }

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }
}
