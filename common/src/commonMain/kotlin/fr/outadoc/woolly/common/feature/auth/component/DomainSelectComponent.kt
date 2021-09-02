package fr.outadoc.woolly.common.feature.auth.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DomainSelectComponent(
    componentContext: ComponentContext,
    private val scope: CoroutineScope
) : ComponentContext by componentContext {

    data class State(
        val error: Throwable? = null,
        val loading: Boolean = false,
        val domain: String = ""
    )

    sealed class Event {
        data class DomainSelectedEvent(val domain: String) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> get() = _events

    fun onDomainTextChanged(domain: String) {
        val currentState = state.value
        _state.value = currentState.copy(domain = domain)
    }

    fun onSubmitDomain() {
        val currentState = state.value
        if (currentState.loading) return

        val domain = currentState.domain.trim()

        val client = MastodonClient {
            this.domain = domain
        }

        _state.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            val error = try {
                client.instance.getInstanceInfo()
                _events.emit(
                    Event.DomainSelectedEvent(domain = domain)
                )
                null
            } catch (e: Throwable) {
                e
            }

            _state.value = currentState.copy(
                loading = false,
                error = error
            )
        }
    }
}
