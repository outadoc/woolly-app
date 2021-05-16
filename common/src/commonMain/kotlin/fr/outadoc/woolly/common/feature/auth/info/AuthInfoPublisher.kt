package fr.outadoc.woolly.common.feature.auth.info

import kotlinx.coroutines.flow.StateFlow

interface AuthInfoPublisher {
    val authInfo: StateFlow<AuthInfo?>
}
