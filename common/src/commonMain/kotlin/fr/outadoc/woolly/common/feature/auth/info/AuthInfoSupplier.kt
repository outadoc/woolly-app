package fr.outadoc.woolly.common.feature.auth.info

import kotlinx.coroutines.flow.StateFlow

interface AuthInfoSupplier {
    val authInfo: StateFlow<AuthInfo?>
}
