package fr.outadoc.woolly.common.feature.publictimeline.component

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getPublicTimelineSource
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.Flow

class PublicTimelineComponent(
    componentContext: ComponentContext,
    statusPagingRepository: StatusPagingRepository,
    private val statusActionRepository: StatusActionRepository
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = getScope()

    val localListState = stateKeeper.consumeListStateOrDefault(key = "local_list_state")
    val globalListState = stateKeeper.consumeListStateOrDefault(key = "global_list_state")

    init {
        stateKeeper.registerListState(key = "local_list_state") { localListState }
        stateKeeper.registerListState(key = "global_list_state") { globalListState }
    }

    val localPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository.getPagingData(
            componentScope,
            factory = { client ->
                client.timelines.getPublicTimelineSource(onlyLocal = true)
            }
        )

    val globalPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository.getPagingData(
            componentScope,
            factory = { client ->
                client.timelines.getPublicTimelineSource()
            }
        )

    fun onLocalStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    fun onGlobalStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        val subScreen = (currentConfig as? AppScreen.PublicTimeline)?.subScreen ?: return
        when (subScreen) {
            PublicTimelineSubScreen.Local -> localListState
            PublicTimelineSubScreen.Global -> globalListState
        }.tryScrollToTop()
    }
}
