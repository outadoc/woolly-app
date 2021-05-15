package fr.outadoc.woolly.common.feature.auth

sealed class AuthState {
    data class Disconnected(val error: Throwable? = null) : AuthState()
    data class InstanceSelected(val domain: String, val error: Throwable? = null) : AuthState()
    data class Authenticated(val authInfo: AuthInfo) : AuthState()
}
