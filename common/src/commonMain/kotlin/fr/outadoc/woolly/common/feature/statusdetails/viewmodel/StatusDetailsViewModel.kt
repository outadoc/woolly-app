package fr.outadoc.woolly.common.feature.statusdetails.viewmodel

import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StatusDetailsViewModel(
    private val viewModelScope: CoroutineScope,
    clientProvider: MastodonClientProvider
) {
    sealed class State {
        object Loading : State()
        object Error : State()
        data class LoadedStatus(
            val status: Status,
            val context: Context?
        ) : State()
    }

    private val statusIdFlow = MutableSharedFlow<String>(replay = 1)

    private val mainStatusFlow: Flow<Status?> =
        combine(clientProvider.mastodonClient, statusIdFlow) { client, statusId ->
            client?.statuses?.getStatus(statusId)
        }

    private val contextFlow: Flow<Context?> =
        combine(clientProvider.mastodonClient, statusIdFlow) { client, statusId ->
            client?.statuses?.getContext(statusId)
        }

    val state: Flow<State> =
        combine(mainStatusFlow, contextFlow) { status, context ->
            if (status == null) State.Error
            else State.LoadedStatus(
                status = status,
                context = context
            )
        }

    fun loadStatus(statusId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            statusIdFlow.emit(statusId)
        }
    }
}
