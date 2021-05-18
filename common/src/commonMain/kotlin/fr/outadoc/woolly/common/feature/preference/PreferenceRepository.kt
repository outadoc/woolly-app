package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState

interface PreferenceRepository {
    var savedAuthenticationState: AuthenticationState
}
