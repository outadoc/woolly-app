package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.woolly.common.feature.auth.info.AuthInfo
import io.ktor.http.*

sealed class AuthState {

    data class Disconnected(
        val error: Throwable? = null,
        val loading: Boolean = false,
        val domain: String = ""
    ) : AuthState()

    data class InstanceSelected(
        val domain: String,
        val authorizeUrl: Url,
        val error: Throwable? = null,
        val loading: Boolean = false
    ) : AuthState()

    data class Authenticated(val authInfo: AuthInfo) : AuthState()
}
