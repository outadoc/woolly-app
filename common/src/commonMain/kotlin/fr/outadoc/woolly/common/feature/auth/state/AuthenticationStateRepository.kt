package fr.outadoc.woolly.common.feature.auth.state

import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow

class AuthenticationStateRepository(
    private val prefs: PreferenceRepository
) : AuthenticationStateSupplier, AuthenticationStateConsumer {

    override val state = MutableStateFlow(prefs.savedAuthenticationState)

    private fun publish(state: AuthenticationState) {
        this.state.value = state
        prefs.savedAuthenticationState = state
    }

    override fun appendCredentials(credentials: UserCredentials) {
        publish(
            state.value.let { currentState ->
                currentState.copy(accounts = currentState.accounts + credentials)
            }
        )
    }

    override fun logoutAll() {
        publish(AuthenticationState(emptyList()))
    }
}
