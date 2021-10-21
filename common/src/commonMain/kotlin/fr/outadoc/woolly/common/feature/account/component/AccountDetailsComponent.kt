package fr.outadoc.woolly.common.feature.account.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Relationship
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.accounts.getStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountDetailsComponent(
    componentContext: ComponentContext,
    statusPagingRepository: StatusPagingRepository,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext, ScrollableComponent {

    sealed class State {
        abstract val isLoading: Boolean
        abstract val accountId: String?

        data class Initial(
            override val isLoading: Boolean = true,
            override val accountId: String? = null
        ) : State()

        data class Error(
            val exception: Exception? = null,
            override val isLoading: Boolean = false,
            override val accountId: String? = null
        ) : State()

        data class LoadedAccount(
            val account: Account,
            val relationship: Relationship,
            override val isLoading: Boolean = false
        ) : State() {

            override val accountId: String
                get() = account.accountId
        }
    }

    private val componentScope = getScope()
    val listState = stateKeeper.consumeListStateOrDefault()

    init {
        stateKeeper.registerListState { listState }
    }

    private val _state = MutableStateFlow<State>(State.Initial())
    val state: Flow<State> = _state.asStateFlow()

    fun State.copy(isLoading: Boolean, accountId: String?): State {
        return when (this) {
            is State.Error -> this.copy(isLoading = isLoading, accountId = accountId)
            is State.Initial -> this.copy(isLoading = isLoading, accountId = accountId)
            is State.LoadedAccount -> this.copy(isLoading = isLoading)
        }
    }

    private val currentAccountIdFlow = MutableSharedFlow<String>(replay = 1)

    init {
        componentScope.launch {
            currentAccountIdFlow.collect { accountId ->
                val client = clientProvider.mastodonClient.value ?: return@collect

                _state.value = _state.value.copy(
                    isLoading = true,
                    accountId = accountId
                )

                val account = client.accounts.getAccount(accountId)
                val relationship =
                    client.accounts.getRelationships(listOf(accountId))?.firstOrNull()

                _state.value =
                    if (account == null || relationship == null) {
                        State.Error(accountId = accountId)
                    } else State.LoadedAccount(
                        account = account,
                        relationship = relationship
                    )
            }
        }
    }

    val timelinePagingItems: Flow<PagingData<Status>> =
        currentAccountIdFlow
            .flatMapLatest { accountId ->
                statusPagingRepository.getPagingData { client ->
                    client.accounts.getStatusesSource(accountId)
                }
            }
            .cachedIn(componentScope)

    fun loadAccount(accountId: String) {
        currentAccountIdFlow.tryEmit(accountId)
    }

    fun refresh() {
        val currentAccountId = _state.value.accountId ?: return
        loadAccount(currentAccountId)
    }

    fun onFollowClick(follow: Boolean) {
        val currentState = _state.value as? State.LoadedAccount ?: return
        val client = clientProvider.mastodonClient.value ?: return

        _state.value = currentState.copy(
            relationship = currentState.relationship.copy(
                isFollowing = follow
            )
        )

        componentScope.launch {
            try {
                when (follow) {
                    true -> client.accounts.followAccount(accountId = currentState.accountId)
                    false -> client.accounts.unfollowAccount(accountId = currentState.accountId)
                }
            } catch (e: Exception) {
                _state.value = currentState
            }
        }
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        listState.tryScrollToTop()
    }
}
