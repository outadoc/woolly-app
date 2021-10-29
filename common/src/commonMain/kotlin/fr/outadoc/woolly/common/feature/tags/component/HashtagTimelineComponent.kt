package fr.outadoc.woolly.common.feature.tags.component

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.timelines.getHashtagTimelineSource
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusDeltaSupplier
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HashtagTimelineComponent(
    componentContext: ComponentContext,
    private val statusPagingRepository: StatusPagingRepository,
    private val statusDeltaSupplier: StatusDeltaSupplier
) : ComponentContext by componentContext, ScrollableComponent {

    data class State(
        val hashtag: String = ""
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val componentScope = getScope()

    val listState = stateKeeper.consumeListStateOrDefault()

    init {
        stateKeeper.registerListState { listState }
    }

    val hashtagPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository.getPagingData(
            componentScope,
            factory = { client ->
                client.timelines.getHashtagTimelineSource(hashtag = state.value.hashtag)
            }
        )

    fun loadHashtag(hashtag: String) {
        _state.value = _state.value.copy(hashtag = hashtag)
        statusPagingRepository.invalidateSource()
    }

    fun onStatusAction(action: StatusAction) {
        statusDeltaSupplier.onStatusAction(action)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        listState.tryScrollToTop()
    }
}
