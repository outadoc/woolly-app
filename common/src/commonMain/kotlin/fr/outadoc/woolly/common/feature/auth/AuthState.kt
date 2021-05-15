package fr.outadoc.woolly.common.feature.auth

import fr.outadoc.mastodonk.api.entity.Token

sealed class AuthState {

    object Disconnected : AuthState()
    data class InstanceSelected(val domain: String) : AuthState()
    data class Authenticated(val domain: String, val token: Token) : AuthState()

    data class Error(val e: Throwable) : AuthState()
}
