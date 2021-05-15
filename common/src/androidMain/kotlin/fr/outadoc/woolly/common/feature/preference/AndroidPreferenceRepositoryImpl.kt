package fr.outadoc.woolly.common.feature.preference

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import fr.outadoc.woolly.common.feature.auth.AuthInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidPreferenceRepositoryImpl(
    scope: CoroutineScope,
    context: Context,
    private val json: Json
) : PreferenceRepository {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_AUTH_INFO = "auth_info"
    }

    override val authInfo = MutableStateFlow<AuthInfo?>(
        prefs.getString(KEY_AUTH_INFO, null)?.let { authInfo ->
            json.decodeFromString(authInfo)
        }
    )

    init {
        scope.launch {
            authInfo.collect { authInfo ->
                prefs.edit { putString(KEY_AUTH_INFO, json.encodeToString(authInfo)) }
            }
        }
    }
}
