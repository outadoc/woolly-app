package fr.outadoc.woolly.common.feature.preference

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState
import fr.outadoc.woolly.common.ui.ColorScheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidPreferenceRepositoryImpl(
    context: Context,
    private val json: Json
) : PreferenceRepository {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_AUTH_STATE = "auth_state"
        private const val KEY_COLOR_SCHEME = "color_scheme"
    }

    override var savedAuthenticationState: AuthenticationState
        get() = prefs.getString(KEY_AUTH_STATE, null)
            ?.let { state -> json.decodeFromString(state) }
            ?: AuthenticationState(emptyList())
        set(value) {
            prefs.edit { putString(KEY_AUTH_STATE, json.encodeToString(value)) }
        }

    override var colorScheme: ColorScheme
        get() = ColorScheme.from(prefs.getString(KEY_COLOR_SCHEME, ColorScheme.Dark.value)!!)
        set(scheme) {
            prefs.edit { putString(KEY_COLOR_SCHEME, scheme.value) }
        }
}
