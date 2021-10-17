package fr.outadoc.woolly.common.feature.notifications.component

import androidx.paging.*
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.notifications.getNotificationsSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.notifications.NotificationsSubScreen
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationsComponent(
    componentContext: ComponentContext,
    pagingConfig: PagingConfig,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = getScope()

    val allListState = stateKeeper.consumeListStateOrDefault()
    val mentionsListState = stateKeeper.consumeListStateOrDefault()

    init {
        stateKeeper.registerListState(key = "all_list_state") { allListState }
        stateKeeper.registerListState(key = "mentions_list_state") { mentionsListState }
    }

    data class State(
        val subScreen: NotificationsSubScreen = NotificationsSubScreen.All
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val allPagingSource: PagingSource<PageInfo, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource()

    val allPagingData: Flow<PagingData<Notification>> =
        Pager(pagingConfig) { allPagingSource }
            .flow.cachedIn(componentScope)

    private val mentionsPagingSource: PagingSource<PageInfo, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource(
                excludeTypes = NotificationType.values().toList() - NotificationType.Mention
            )

    val mentionsPagingData: Flow<PagingData<Notification>> =
        Pager(pagingConfig) { mentionsPagingSource }
            .flow.cachedIn(componentScope)

    suspend fun onSubScreenSelected(subScreen: NotificationsSubScreen) {
        val currentState = _state.value
        if (currentState.subScreen == subScreen) {
            scrollToTop()
        }
        _state.value = currentState.copy(subScreen = subScreen)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        allListState.tryScrollToTop()
    }
}
