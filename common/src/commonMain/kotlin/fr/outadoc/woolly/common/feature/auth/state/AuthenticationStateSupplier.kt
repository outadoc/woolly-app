package fr.outadoc.woolly.common.feature.auth.state

import kotlinx.coroutines.flow.Flow

interface AuthenticationStateSupplier {
    val state: Flow<AuthenticationState>
}
