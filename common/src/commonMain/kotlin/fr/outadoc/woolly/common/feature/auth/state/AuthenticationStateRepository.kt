package fr.outadoc.woolly.common.feature.auth.state

import fr.outadoc.woolly.common.feature.preference.PreferenceRepository

class AuthenticationStateRepository(
    private val prefs: PreferenceRepository
) : AuthenticationStateConsumer {

    override suspend fun appendCredentials(credentials: UserCredentials) {
        prefs.updatePreferences { current ->
            current.copy(
                authenticationState = current.authenticationState.copy(
                    accounts = current.authenticationState.accounts + credentials
                )
            )
        }
    }

    override suspend fun logoutAll() {
        prefs.updatePreferences { current ->
            current.copy(authenticationState = AuthenticationState())
        }
    }
}
