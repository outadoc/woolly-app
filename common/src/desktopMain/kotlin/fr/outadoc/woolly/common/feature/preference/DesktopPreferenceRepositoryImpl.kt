package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.AuthInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.prefs.Preferences

class DesktopPreferenceRepositoryImpl(
    scope: CoroutineScope,
    private val json: Json
) : PreferenceRepository {

    private val prefs = Preferences.userNodeForPackage(PreferenceRepository::class.java)

    companion object {
        private const val KEY_AUTH_INFO = "auth_info"
    }

    override val authInfo = MutableStateFlow<AuthInfo?>(
        prefs.get(KEY_AUTH_INFO, null)?.let { authInfo ->
            json.decodeFromString(authInfo)
        }
    )

    init {
        scope.launch {
            authInfo.collect { authInfo ->
                prefs.put(KEY_AUTH_INFO, json.encodeToString(authInfo))
            }
        }
    }
}
