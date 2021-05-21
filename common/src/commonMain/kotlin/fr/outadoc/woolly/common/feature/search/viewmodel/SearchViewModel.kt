package fr.outadoc.woolly.common.feature.search.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchAccountsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchHashtagsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    data class UiState(val query: String = "")

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _state

    private val pagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    private val clientAndStateFlow: Flow<Pair<MastodonClient, UiState>> =
        combine(
            clientProvider.mastodonClient.filterNotNull(),
            _state
        ) { client, state -> client to state }

    val statusPagingItems: Flow<PagingData<Status>> =
        clientAndStateFlow
            .flatMapLatest { (client, state) ->
                Pager(pagingConfig) {
                    client.search.searchStatusesSource(q = state.query)
                }.flow
            }.cachedIn(viewModelScope)

    val accountsPagingItems: Flow<PagingData<Account>> =
        clientAndStateFlow
            .flatMapLatest { (client, state) ->
                Pager(pagingConfig) {
                    client.search.searchAccountsSource(q = state.query)
                }.flow
            }.cachedIn(viewModelScope)

    val hashtagsPagingItems: Flow<PagingData<Tag>> =
        clientAndStateFlow
            .flatMapLatest { (client, state) ->
                Pager(pagingConfig) {
                    client.search.searchHashtagsSource(q = state.query)
                }.flow
            }.cachedIn(viewModelScope)

    fun onSearchTermChanged(term: String) {
        _state.value = _state.value.copy(query = term)
    }
}
