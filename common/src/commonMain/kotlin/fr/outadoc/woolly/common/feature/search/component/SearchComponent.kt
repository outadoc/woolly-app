package fr.outadoc.woolly.common.feature.search.component

import androidx.paging.*
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchAccountsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchHashtagsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class SearchComponent(
    componentContext: ComponentContext,
    pagingConfig: PagingConfig,
    private val clientProvider: MastodonClientProvider,
    private val statusActionRepository: StatusActionRepository,
    private val statusPagingRepository: StatusPagingRepository
) : ComponentContext by componentContext, ScrollableComponent {

    private val componentScope = getScope()

    data class State(val query: String = "")

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingTags: Flow<List<Tag>> =
        clientProvider.mastodonClient
            .filterNotNull()
            .mapLatest { client -> client.trends.getTrends() }
            .flowOn(Dispatchers.IO)

    val statusListState = stateKeeper.consumeListStateOrDefault(key = "status_list_state")
    val accountsListState = stateKeeper.consumeListStateOrDefault(key = "accounts_list_state")
    val hashtagsListState = stateKeeper.consumeListStateOrDefault(key = "hashtags_list_state")

    init {
        stateKeeper.registerListState(key = "status_list_state") { statusListState }
        stateKeeper.registerListState(key = "accounts_list_state") { accountsListState }
        stateKeeper.registerListState(key = "hashtags_list_state") { hashtagsListState }
    }

    val statusPagingItems: Flow<PagingData<Status>> =
        statusPagingRepository.getPagingData(
            componentScope,
            factory = { client ->
                client.search.searchStatusesSource(q = state.value.query)
            }
        )

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
            .cachedIn(componentScope)

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
            .cachedIn(componentScope)

    fun onSearchTermChanged(term: String) {
        _state.value = _state.value.copy(query = term)

        statusPagingRepository.invalidateSource()
        _latestAccountsPagingSource?.invalidate()
        _latestHashtagsPagingSource?.invalidate()
    }

    fun onStatusAction(action: StatusAction) {
        statusActionRepository.onStatusAction(action)
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        val subScreen = (currentConfig as? AppScreen.Search)?.subScreen ?: return
        when (subScreen) {
            SearchSubScreen.Statuses -> statusListState
            SearchSubScreen.Accounts -> accountsListState
            SearchSubScreen.Hashtags -> hashtagsListState
        }.tryScrollToTop()
    }

    init {
        backPressedDispatcher.register {
            // Handle the back button.
            // Return true to consume the event, or false to allow other consumers.
            _state.value.query.isNotEmpty().also { isNotEmpty ->
                if (isNotEmpty) {
                    onSearchTermChanged(term = "")
                }
            }
        }
    }
}
