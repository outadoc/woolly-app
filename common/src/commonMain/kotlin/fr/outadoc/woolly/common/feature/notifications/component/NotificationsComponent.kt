package fr.outadoc.woolly.common.feature.notifications.component

import androidx.paging.*
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.notifications.getNotificationsSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class NotificationsComponent(
    componentContext: ComponentContext,
    pagingConfig: PagingConfig,
    componentScope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext {

    private val pagingSource: PagingSource<PageInfo, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource()

    val pagingData: Flow<PagingData<Notification>> =
        Pager(pagingConfig) { pagingSource }
            .flow
            .cachedIn(componentScope)
}
