package fr.outadoc.woolly.common.feature.notifications.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.doOnDestroy
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.notifications.getNotificationsSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.feature.mainrouter.component.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.screen.AppScreen
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationsComponent(
    componentContext: ComponentContext,
    pagingConfig: PagingConfig,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = MainScope()

    // TODO save state
    val listState = LazyListState()

    private val pagingSource: PagingSource<PageInfo, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource()

    val pagingData: Flow<PagingData<Notification>> =
        Pager(pagingConfig) { pagingSource }
            .flow
            .cachedIn(componentScope)

    override fun scrollToTop(currentConfig: AppScreen?) {
        componentScope.launch {
            listState.tryScrollToTop()
        }
    }

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }
}
