package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val authProxyRepository: AuthProxyRepository,
    private val preferenceRepository: PreferenceRepository
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Disconnected())
    val authState: StateFlow<AuthState> = _authState

    suspend fun onDomainSelected(domain: String) {
        val client = MastodonClient {
            this.domain = domain.trim()
        }

        withContext(Dispatchers.IO) {
            _authState.value = try {
                client.instance.getInstanceInfo()
                AuthState.InstanceSelected(domain.trim())
            } catch (e: Throwable) {
                AuthState.Disconnected(e)
            }
        }
    }

    suspend fun onAuthCodeReceived(domain: String, code: String) {
        withContext(Dispatchers.IO) {
            _authState.value = try {
                val token = authProxyRepository.getToken(domain.trim(), code)
                val authInfo = AuthInfo(domain.trim(), token)

                // Save auth info to disk
                preferenceRepository.authInfo.value = authInfo

                // We're authenticated!
                AuthState.Authenticated(authInfo)
            } catch (e: Throwable) {
                AuthState.InstanceSelected(domain, e)
            }
        }
    }
}
