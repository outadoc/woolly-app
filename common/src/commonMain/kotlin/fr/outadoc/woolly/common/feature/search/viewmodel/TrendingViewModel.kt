package fr.outadoc.woolly.common.feature.search.viewmodel

import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingViewModel(clientProvider: MastodonClientProvider) {

    val trendingTags: Flow<List<Tag>> =
        clientProvider.mastodonClient
            .filterNotNull()
            .mapLatest { client -> client.trends.getTrends() }
            .flowOn(Dispatchers.IO)
}