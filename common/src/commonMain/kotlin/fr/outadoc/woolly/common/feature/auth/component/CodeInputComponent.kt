package fr.outadoc.woolly.common.feature.auth.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.state.UserCredentials
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CodeInputComponent(
    componentContext: ComponentContext,
    private val scope: CoroutineScope,
    private val authProxyRepository: AuthProxyRepository
): ComponentContext by componentContext {

    data class State(
        val error: Throwable? = null,
        val loading: Boolean = false
    )

    sealed class Event {
        data class Authenticated(val credentials: UserCredentials) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun onAuthCodeReceived(domain: String, code: String) {
        val currentState = state.value
        if (currentState.loading) return

        _state.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            val error = try {
                _events.emit(
                    Event.Authenticated(
                        UserCredentials(
                            domain = domain,
                            token = authProxyRepository.getToken(domain, code)
                        )
                    )
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

    fun getAuthorizeUrl(domain: String): Url {
        return authProxyRepository.getAuthorizeUrl(domain)
    }
}
