package fr.outadoc.woolly.common.feature.composer

import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import kotlinx.coroutines.flow.StateFlow

interface StatusPoster {

    data class State(
        val posting: Set<StatusCreate> = emptySet(),
        val error: Set<StatusCreate> = emptySet()
    )

    val state: StateFlow<State>
    suspend fun enqueueStatus(statusCreate: StatusCreate)
    suspend fun retryAll()
}
