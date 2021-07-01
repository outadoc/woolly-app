package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState
import fr.outadoc.woolly.common.ui.ColorScheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class DesktopPreferenceRepositoryImpl(private val json: Json) : PreferenceRepository {

    private val prefs = Preferences.userRoot()

    companion object {
        private const val KEY_AUTH_STATE = "auth_state"
        private const val KEY_COLOR_SCHEME = "color_scheme"
    }

    override var savedAuthenticationState: AuthenticationState
        get() = prefs.get(KEY_AUTH_STATE, null)
            ?.let { state -> json.decodeFromString(state) }
            ?: AuthenticationState(emptyList())
        set(value) {
            prefs.put(KEY_AUTH_STATE, json.encodeToString(value))
        }

    override var colorScheme: ColorScheme
        get() = ColorScheme.from(prefs.get(KEY_COLOR_SCHEME, ColorScheme.Dark.value)!!)
        set(scheme) {
            prefs.put(KEY_COLOR_SCHEME, scheme.value)
        }
}
