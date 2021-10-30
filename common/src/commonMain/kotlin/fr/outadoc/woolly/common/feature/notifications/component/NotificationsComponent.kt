package fr.outadoc.woolly.common.feature.notifications.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.*
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
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

    data class State(
        val subScreen: NotificationsSubScreen,
        val listState: LazyListState,
        val pagingData: Flow<PagingData<Notification>>
    )

    private val allPagingSource: PagingSource<*, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource()

    private val allState = State(
        subScreen = NotificationsSubScreen.All,
        listState = stateKeeper.consumeListStateOrDefault(),
        pagingData = Pager(pagingConfig) { allPagingSource }.flow.cachedIn(componentScope)
    )

    private val mentionsPagingSource: PagingSource<*, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource(
                excludeTypes = NotificationType.values().toList() - NotificationType.Mention
            )

    private val mentionsState = State(
        subScreen = NotificationsSubScreen.MentionsOnly,
        listState = stateKeeper.consumeListStateOrDefault(),
        pagingData = Pager(pagingConfig) { mentionsPagingSource }.flow.cachedIn(componentScope)
    )

    init {
        stateKeeper.registerListState(key = "all_list_state") { allState.listState }
        stateKeeper.registerListState(key = "mentions_list_state") { mentionsState.listState }
    }

    private val _state = MutableStateFlow(allState)
    val state = _state.asStateFlow()

    suspend fun onSubScreenSelected(subScreen: NotificationsSubScreen) {
        val currentState = _state.value
        if (currentState.subScreen == subScreen) {
            scrollToTop()
            return
        }

        _state.value = when (subScreen) {
            NotificationsSubScreen.All -> allState
            NotificationsSubScreen.MentionsOnly -> mentionsState
        }
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        state.value.listState.tryScrollToTop()
    }
}
