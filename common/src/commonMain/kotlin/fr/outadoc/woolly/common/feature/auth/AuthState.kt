package fr.outadoc.woolly.common.feature.auth

sealed class AuthState {

    object Disconnected : AuthState()
    data class InstanceSelected(val domain: String) : AuthState()
    data class Authenticated(val authInfo: AuthInfo) : AuthState()
    data class Error(val e: Throwable) : AuthState()
}
