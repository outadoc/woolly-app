package fr.outadoc.woolly.common.feature.preference

import fr.outadoc.woolly.common.feature.auth.info.AuthInfo

interface PreferenceRepository {
    var savedAuthInfo: AuthInfo?
}
