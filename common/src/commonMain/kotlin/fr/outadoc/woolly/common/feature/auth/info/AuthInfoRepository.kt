package fr.outadoc.woolly.common.feature.auth.info

import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow

class AuthInfoRepository(
    private val prefs: PreferenceRepository
) : AuthInfoSupplier, AuthInfoConsumer {

    override val authInfo = MutableStateFlow(prefs.savedAuthInfo)

    override fun publish(authInfo: AuthInfo?) {
        this.authInfo.value = authInfo
        prefs.savedAuthInfo = authInfo
    }
}
