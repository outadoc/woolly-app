package fr.outadoc.woolly.common.feature.composer

import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import kotlinx.coroutines.flow.StateFlow

interface StatusPoster {

    sealed class State {
        object Idle : State()
        object Posting : State()
        object Error : State()
    }

    val state: StateFlow<State>
    fun enqueueStatus(statusCreate: StatusCreate)
    fun retryAll()
}
