package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(

    @SerialName("auth_state")
    val authenticationState: AuthenticationState = AuthenticationState(),

    @SerialName("color_scheme")
    val preferredTheme: PreferredTheme = PreferredTheme.FollowSystem
)