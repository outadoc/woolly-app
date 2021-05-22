package fr.outadoc.woolly.common.feature.search.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchAccountsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchHashtagsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.ui.StatusAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    pagingConfig: PagingConfig,
    private val viewModelScope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) {
    data class UiState(val query: String = "")

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState>
        get() = _state

    private val statusPagingRepository: StatusPagingRepository =
        StatusPagingRepository(pagingConfig, clientProvider) { client ->
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
        viewModelScope.launch {
            statusPagingRepository.onStatusAction(action)
        }
    }

    init {
        viewModelScope.launch {
            statusPagingRepository.actionObserver.collect()
        }
    }
}
