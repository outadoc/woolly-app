package fr.outadoc.woolly.common.feature.publictimeline.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PublicTimelineComponent(
    componentContext: ComponentContext,
    statusPagingRepository: StatusPagingRepository,
    private val statusActionRepository: StatusActionRepository
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = getScope()

    val localListState = stateKeeper.consumeListStateOrDefault(key = "local_list_state")
    val globalListState = stateKeeper.consumeListStateOrDefault(key = "global_list_state")

    data class State(
        val subScreen: PublicTimelineSubScreen = PublicTimelineSubScreen.Local
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        stateKeeper.registerListState(key = "local_list_state") { localListState }
        stateKeeper.registerListState(key = "global_list_state") { globalListState }
    }

    val localPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository
            .getPagingData { client -> client.timelines.getPublicTimelineSource(onlyLocal = true) }
            .cachedIn(componentScope)

    val globalPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository
            .getPagingData { client -> client.timelines.getPublicTimelineSource() }
            .cachedIn(componentScope)

    fun onLocalStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    fun onGlobalStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    suspend fun onSubScreenSelected(subScreen: PublicTimelineSubScreen) {
        val currentState = _state.value
        if (currentState.subScreen == subScreen) {
            scrollToTop()
        }
        _state.value = currentState.copy(subScreen = subScreen)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        when (state.value.subScreen) {
            PublicTimelineSubScreen.Local -> localListState
            PublicTimelineSubScreen.Global -> globalListState
        }.tryScrollToTop()
    }
}
