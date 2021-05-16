package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.info.AuthInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class DesktopPreferenceRepositoryImpl(private val json: Json) : PreferenceRepository {

    private val prefs = Preferences.userRoot()

    companion object {
        private const val KEY_AUTH_INFO = "auth_info"
    }

    override var savedAuthInfo: AuthInfo?
        get() = prefs.get(KEY_AUTH_INFO, null)?.let { authInfo ->
            json.decodeFromString(authInfo)
        }
        set(value) {
            prefs.put(KEY_AUTH_INFO, json.encodeToString(value))
        }
}
