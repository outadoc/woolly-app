package fr.outadoc.woolly.common.feature.auth.state

import kotlinx.coroutines.flow.StateFlow

interface AuthenticationStateSupplier {
    val state: StateFlow<AuthenticationState>
}
