package fr.outadoc.woolly.common.feature.statusdetails.viewmodel

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
        data class LoadedStatus(val status: Status) : State()
    }

    private val statusIdFlow = MutableSharedFlow<String>(replay = 1)

    val state: Flow<State> = combine(clientProvider.mastodonClient, statusIdFlow) { client, statusId ->
            when (val status = client?.statuses?.getStatus(statusId)) {
                null -> State.Error
                else -> State.LoadedStatus(status)
            }
        }

    fun loadStatus(statusId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            statusIdFlow.emit(statusId)
        }
    }
}
