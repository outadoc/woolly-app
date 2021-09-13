package fr.outadoc.woolly.common.feature.account.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Relationship
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountDetailsComponent(
    componentContext: ComponentContext,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext {

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

    private val _state = MutableStateFlow<State>(State.Initial())
    val state: Flow<State> = _state.asStateFlow()

    fun State.copy(isLoading: Boolean, accountId: String?): State {
        return when (this) {
            is State.Error -> this.copy(isLoading = isLoading, accountId = accountId)
            is State.Initial -> this.copy(isLoading = isLoading, accountId = accountId)
            is State.LoadedAccount -> this.copy(isLoading = isLoading)
        }
    }

    fun loadAccount(accountId: String) {
        val client = clientProvider.mastodonClient.value ?: return

        componentScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                accountId = accountId
            )

            val account = client.accounts.getAccount(accountId)
            val relationship = client.accounts.getRelationships(listOf(accountId))?.firstOrNull()

            _state.value =
                if (account == null || relationship == null) {
                    State.Error(accountId = accountId)
                } else State.LoadedAccount(
                    account = account,
                    relationship = relationship
                )
        }
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
}
