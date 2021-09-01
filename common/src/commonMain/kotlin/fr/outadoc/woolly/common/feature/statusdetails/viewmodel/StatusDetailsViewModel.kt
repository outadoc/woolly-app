package fr.outadoc.woolly.common.feature.statusdetails.viewmodel

import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.flow.*

class StatusDetailsViewModel(clientProvider: MastodonClientProvider) {
    sealed class State {
        object Loading : State()
        object Error : State()
        data class LoadedStatus(
            val status: Status,
            val context: Context
        ) : State()
    }

    private val statusIdFlow = MutableSharedFlow<String>(replay = 1)

    val state: Flow<State> = flow {
        combine(clientProvider.mastodonClient, statusIdFlow) { client, statusId ->
            emit(State.Loading)

            if (client == null) emit(State.Error)
            else {
                val status = client.statuses.getStatus(statusId)
                val context = client.statuses.getContext(statusId)
                    ?: Context(
                        ancestors = emptyList(),
                        descendants = emptyList()
                    )

                emit(
                    if (status == null) State.Error
                    else State.LoadedStatus(
                        status = status,
                        context = context
                    )
                )
            }
        }.collect()
    }

    fun loadStatus(statusId: String) {
        statusIdFlow.tryEmit(statusId)
    }
}
