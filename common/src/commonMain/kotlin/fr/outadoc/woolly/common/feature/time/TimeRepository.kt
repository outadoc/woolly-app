package fr.outadoc.woolly.common.feature.time

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TimeRepository {

    val currentTime: Flow<Instant> = flow {
        while (currentCoroutineContext().isActive) {
            delay(1_000)
            emit(Clock.System.now())
        }
    }
}
