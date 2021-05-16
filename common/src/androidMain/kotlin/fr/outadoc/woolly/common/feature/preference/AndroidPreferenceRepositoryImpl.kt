package fr.outadoc.woolly.common.feature.preference

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import fr.outadoc.woolly.common.feature.auth.info.AuthInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidPreferenceRepositoryImpl(
    context: Context,
    private val json: Json
) : PreferenceRepository {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_AUTH_INFO = "auth_info"
    }

    override var savedAuthInfo: AuthInfo?
        get() = prefs.getString(KEY_AUTH_INFO, null)?.let { authInfo ->
            json.decodeFromString(authInfo)
        }
        set(value) {
            prefs.edit { putString(KEY_AUTH_INFO, json.encodeToString(value)) }
        }
}
