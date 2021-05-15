package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AuthViewModel(private val authProxyRepository: AuthProxyRepository) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Disconnected)
    val authState: Flow<AuthState> = _authState

    suspend fun onDomainSelected(domain: String) {
        val client = MastodonClient {
            this.domain = domain.trim()
        }

        withContext(Dispatchers.IO) {
            _authState.value = try {
                client.instance.getInstanceInfo()
                AuthState.InstanceSelected(domain.trim())
            } catch (e: Throwable) {
                AuthState.Error(e)
            }
        }
    }

    suspend fun onAuthCodeReceived(domain: String, code: String) {
        withContext(Dispatchers.IO) {
            _authState.value = try {
                val token = authProxyRepository.getToken(domain.trim(), code)
                AuthState.Authenticated(
                    AuthInfo(domain.trim(), token)
                )
            } catch (e: Throwable) {
                AuthState.Error(e)
            }
        }
    }
}
