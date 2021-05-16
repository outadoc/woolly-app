package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.auth.info.AuthInfo
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoSubscriber
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val scope: CoroutineScope,
    private val authProxyRepository: AuthProxyRepository,
    private val authInfoSubscriber: AuthInfoSubscriber
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Disconnected())
    val authState: StateFlow<AuthState> = _authState

    fun onDomainTextChanged(domain: String) {
        val currentState = authState.value as? AuthState.Disconnected ?: return
        _authState.value = currentState.copy(domain = domain)
    }

    fun onSubmitDomain() {
        val currentState = authState.value as? AuthState.Disconnected ?: return
        if (currentState.loading) return

        val domain = currentState.domain.trim()

        val client = MastodonClient {
            this.domain = domain
        }

        _authState.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _authState.value = try {
                client.instance.getInstanceInfo()
                AuthState.InstanceSelected(
                    domain = domain,
                    authorizeUrl = authProxyRepository.getAuthorizeUrl(domain)
                )
            } catch (e: Throwable) {
                currentState.copy(loading = false, error = e)
            }
        }
    }

    fun onAuthCodeReceived(code: String) {
        val currentState = authState.value as? AuthState.InstanceSelected ?: return
        if (currentState.loading) return

        _authState.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _authState.value = try {
                val token = authProxyRepository.getToken(currentState.domain, code)
                val authInfo = AuthInfo(currentState.domain, token)

                // We're authenticated!
                authInfoSubscriber.publish(authInfo)
                AuthState.Authenticated(authInfo)
            } catch (e: Throwable) {
                currentState.copy(error = e)
            }
        }
    }

    fun onBackPressed() {
        val currentState = authState.value as? AuthState.InstanceSelected ?: return
        _authState.value = AuthState.Disconnected(domain = currentState.domain)
    }
}
