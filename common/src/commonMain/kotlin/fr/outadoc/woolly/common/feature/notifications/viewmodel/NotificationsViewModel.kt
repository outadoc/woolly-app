package fr.outadoc.woolly.common.feature.notifications.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.notifications.getNotificationsSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class NotificationsViewModel(
    pagingConfig: PagingConfig,
    viewModelScope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) {
    private val pagingSource: PagingSource<PageInfo, Notification>
        get() = clientProvider
            .latestClientOrThrow
            .notifications.getNotificationsSource()

    val pagingData: Flow<PagingData<Notification>> =
        Pager(pagingConfig) { pagingSource }
            .flow
            .cachedIn(viewModelScope)
}
