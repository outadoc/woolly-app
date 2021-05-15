package fr.outadoc.woolly.common.feature.auth

sealed class AuthState {

    data class Disconnected(
        val error: Throwable? = null,
        val loading: Boolean = false
    ) : AuthState()

    data class InstanceSelected(
        val domain: String,
        val error: Throwable? = null,
        val loading: Boolean = false
    ) : AuthState()

    data class Authenticated(val authInfo: AuthInfo) : AuthState()
}
