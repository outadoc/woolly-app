package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.AuthInfo
import kotlinx.coroutines.flow.MutableStateFlow

interface PreferenceRepository {
    val authInfo: MutableStateFlow<AuthInfo?>
}
