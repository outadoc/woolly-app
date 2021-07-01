package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState
import fr.outadoc.woolly.common.ui.ColorScheme

interface PreferenceRepository {
    var savedAuthenticationState: AuthenticationState
    var colorScheme: ColorScheme
}
