package fr.outadoc.woolly.common.feature.statusdetails.viewmodel

import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.flow.*

class StatusDetailsViewModel(clientProvider: MastodonClientProvider) {
    sealed class State(
        open val isLoading: Boolean
    ) {
        data class Initial(
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class Error(
            override val isLoading: Boolean = false
        ) : State(isLoading)

        data class LoadedStatus(
            val status: Status,
            val context: Context,
            override val isLoading: Boolean = false
        ) : State(isLoading)
    }

    private val statusIdFlow = MutableSharedFlow<String>(replay = 1)

    val state: Flow<State> = flow {
        var currentState: State = State.Initial(isLoading = true)

        combine(clientProvider.mastodonClient, statusIdFlow) { client, statusId ->
            emit(currentState.copy(isLoading = true))

            val nextState = if (client == null) State.Error()
            else {
                val status = client.statuses.getStatus(statusId)
                val context = client.statuses.getContext(statusId)
                    ?: Context(
                        ancestors = emptyList(),
                        descendants = emptyList()
                    )

                if (status == null) State.Error()
                else State.LoadedStatus(
                    status = status,
                    context = context
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
            is State.LoadedStatus -> this.copy(isLoading = isLoading)
        }
    }

    fun loadStatus(statusId: String) {
        statusIdFlow.tryEmit(statusId)
    }
}
