package fr.outadoc.woolly.common.feature.auth.viewmodel

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.auth.info.AuthInfo
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoConsumer
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val scope: CoroutineScope,
    private val authProxyRepository: AuthProxyRepository,
    private val authInfoConsumer: AuthInfoConsumer
) {
    sealed class State {

        data class Disconnected(
            val error: Throwable? = null,
            val loading: Boolean = false,
            val domain: String = ""
        ) : State()

        data class InstanceSelected(
            val domain: String,
            val authorizeUrl: Url,
            val error: Throwable? = null,
            val loading: Boolean = false
        ) : State()

        data class Authenticated(val authInfo: AuthInfo) : State()
    }

    private val _state = MutableStateFlow<State>(State.Disconnected())
    val state: StateFlow<State> = _state

    fun onDomainTextChanged(domain: String) {
        val currentState = state.value as? State.Disconnected ?: return
        _state.value = currentState.copy(domain = domain)
    }

    fun onSubmitDomain() {
        val currentState = state.value as? State.Disconnected ?: return
        if (currentState.loading) return

        val domain = currentState.domain.trim()

        val client = MastodonClient {
            this.domain = domain
        }

        _state.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _state.value = try {
                client.instance.getInstanceInfo()
                State.InstanceSelected(
                    domain = domain,
                    authorizeUrl = authProxyRepository.getAuthorizeUrl(domain)
                )
            } catch (e: Throwable) {
                currentState.copy(loading = false, error = e)
            }
        }
    }

    fun onAuthCodeReceived(code: String) {
        val currentState = state.value as? State.InstanceSelected ?: return
        if (currentState.loading) return

        _state.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _state.value = try {
                val token = authProxyRepository.getToken(currentState.domain, code)
                val authInfo = AuthInfo(currentState.domain, token)

                // We're authenticated!
                authInfoConsumer.publish(authInfo)
                State.Authenticated(authInfo)
            } catch (e: Throwable) {
                currentState.copy(error = e)
            }
        }
    }

    fun onBackPressed() {
        val currentState = state.value as? State.InstanceSelected ?: return
        _state.value = State.Disconnected(domain = currentState.domain)
    }
}
