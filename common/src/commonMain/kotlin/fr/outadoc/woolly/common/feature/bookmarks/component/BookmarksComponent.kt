package fr.outadoc.woolly.common.feature.bookmarks.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.accounts.getBookmarksSource
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.Flow

class BookmarksComponent(
    componentContext: ComponentContext,
    statusPagingRepository: StatusPagingRepository,
    private val statusActionRepository: StatusActionRepository
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = getScope()

    val listState = stateKeeper.consumeListStateOrDefault()

    init {
        stateKeeper.registerListState { listState }
    }

    val bookmarksPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository
            .getPagingData { client ->
                client.bookmarks.getBookmarksSource()
            }
            .cachedIn(componentScope)

    fun onStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        listState.tryScrollToTop()
    }
}
