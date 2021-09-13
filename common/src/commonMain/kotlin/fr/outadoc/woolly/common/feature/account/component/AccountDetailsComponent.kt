package fr.outadoc.woolly.common.feature.account.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Relationship
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountDetailsComponent(
    componentContext: ComponentContext,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext {

    sealed class State(
        open val isLoading: Boolean
    ) {
        data class Initial(
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class Error(
            val exception: Exception? = null,
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class LoadedAccount(
            val account: Account,
            val relationship: Relationship,
            override val isLoading: Boolean = false
        ) : State(isLoading)
    }

    private val componentScope = getScope()

    private val accountIdFlow = MutableSharedFlow<String>(replay = 1)

    val state: Flow<State> = flow {
        var currentState: State = State.Initial(isLoading = true)

        combine(clientProvider.mastodonClient, accountIdFlow) { client, accountId ->
            emit(currentState.copy(isLoading = true))

            val nextState = if (client == null) State.Error()
            else {
                val account = client.accounts.getAccount(accountId)
                    ?: return@combine State.Error()

                val relationship = client.accounts.getRelationships(listOf(accountId))
                    ?.firstOrNull()
                    ?: return@combine State.Error()

                State.LoadedAccount(
                    account = account,
                    relationship = relationship
                )
            }

            currentState = nextState
            emit(nextState)

        }.collect()
    }

    fun State.copy(isLoading: Boolean): State {
        return when (this) {
            is State.Error -> this.copy(isLoading = isLoading)
            is State.Initial -> this.copy(isLoading = isLoading)
            is State.LoadedAccount -> this.copy(isLoading = isLoading)
        }
    }

    fun loadAccount(accountId: String) {
        accountIdFlow.tryEmit(accountId)
    }

    fun refresh() {
        accountIdFlow.replayCache.lastOrNull()?.let { currentId ->
            accountIdFlow.tryEmit(currentId)
        }
    }

    fun onFollowClick(follow: Boolean) {
        componentScope.launch {
            clientProvider.mastodonClient.value?.accounts?.let { api ->
                when (follow) {
                    true -> TODO()
                    false -> TODO()
                }
            }
        }
    }
}
