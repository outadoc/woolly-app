package fr.outadoc.woolly.common.feature.status

import kotlinx.coroutines.flow.StateFlow

interface StatusDeltaConsumer {
    val statusDeltas: StateFlow<Map<String, StatusDelta>>
}
