package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val scope: CoroutineScope,
    private val authProxyRepository: AuthProxyRepository,
    private val preferenceRepository: PreferenceRepository
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Disconnected())
    val authState: StateFlow<AuthState> = _authState

    fun onDomainSelected(domain: String) {
        val currentState = authState.value as? AuthState.Disconnected ?: return
        val client = MastodonClient {
            this.domain = domain.trim()
        }

        _authState.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _authState.value = try {
                client.instance.getInstanceInfo()
                AuthState.InstanceSelected(domain.trim())
            } catch (e: Throwable) {
                currentState.copy(loading = false, error = e)
            }
        }
    }

    fun onAuthCodeReceived(domain: String, code: String) {
        val currentState = authState.value as? AuthState.InstanceSelected ?: return

        _authState.value = currentState.copy(loading = true)

        scope.launch(Dispatchers.IO) {
            _authState.value = try {
                val token = authProxyRepository.getToken(domain.trim(), code)
                val authInfo = AuthInfo(domain.trim(), token)

                // Save auth info to disk
                preferenceRepository.authInfo.value = authInfo

                // We're authenticated!
                AuthState.Authenticated(authInfo)
            } catch (e: Throwable) {
                currentState.copy(error = e)
            }
        }
    }
}
