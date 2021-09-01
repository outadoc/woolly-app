package fr.outadoc.woolly.common.feature.search.viewmodel

import androidx.paging.*
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchAccountsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchHashtagsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(
    pagingConfig: PagingConfig,
    viewModelScope: CoroutineScope,
    private val clientProvider: MastodonClientProvider,
    statusActionRepository: StatusActionRepository
) {
    data class UiState(val query: String = "")

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _state

    private val statusPagingRepository = StatusPagingRepository(
        pagingConfig,
        clientProvider,
        statusActionRepository
    ) { client ->
        client.search.searchStatusesSource(q = state.value.query)
    }

    val statusPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository
            .pagingData
            .cachedIn(viewModelScope)

    private var _latestAccountsPagingSource: PagingSource<PageInfo, Account>? = null
    private val accountsPagingSource: PagingSource<PageInfo, Account>
        get() = clientProvider
            .latestClientOrThrow
            .search.searchAccountsSource(q = state.value.query)
            .also { newSource ->
                _latestAccountsPagingSource = newSource
            }

    val accountsPagingItems: Flow<PagingData<Account>> =
        Pager(pagingConfig) { accountsPagingSource }
            .flow
            .cachedIn(viewModelScope)

    private var _latestHashtagsPagingSource: PagingSource<PageInfo, Tag>? = null
    private val hashtagsPagingSource: PagingSource<PageInfo, Tag>
        get() = clientProvider
            .latestClientOrThrow
            .search.searchHashtagsSource(q = state.value.query)
            .also { newSource ->
                _latestHashtagsPagingSource = newSource
            }

    val hashtagsPagingItems: Flow<PagingData<Tag>> =
        Pager(pagingConfig) { hashtagsPagingSource }
            .flow
            .cachedIn(viewModelScope)

    fun onSearchTermChanged(term: String) {
        _state.value = _state.value.copy(query = term)

        statusPagingRepository.invalidate()
        _latestAccountsPagingSource?.invalidate()
        _latestHashtagsPagingSource?.invalidate()
    }

    fun onStatusAction(action: StatusAction) {
        statusPagingRepository.onStatusAction(action)
    }
}
