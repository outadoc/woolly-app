package fr.outadoc.woolly.common.feature.account.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.flow.*

class AccountDetailsComponent(
    componentContext: ComponentContext,
    clientProvider: MastodonClientProvider
) : ComponentContext by componentContext {

    sealed class State(
        open val isLoading: Boolean
    ) {
        data class Initial(
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class Error(
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class LoadedAccount(
            val account: Account,
            override val isLoading: Boolean = false
        ) : State(isLoading)
    }

    private val accountIdFlow = MutableSharedFlow<String>(replay = 1)

    val state: Flow<State> = flow {
        var currentState: State = State.Initial(isLoading = true)

        combine(clientProvider.mastodonClient, accountIdFlow) { client, accountId ->
            emit(currentState.copy(isLoading = true))

            val nextState = if (client == null) State.Error()
            else {
                val account = client.accounts.getAccount(accountId)

                if (account == null) State.Error()
                else State.LoadedAccount(
                    account = account
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
}
