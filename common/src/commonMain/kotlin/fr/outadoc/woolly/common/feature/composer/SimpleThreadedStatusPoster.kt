package fr.outadoc.woolly.common.feature.composer

import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SimpleThreadedStatusPoster(
    scope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) : StatusPoster {

    private data class PublishQueue(
        val posting: Set<StatusCreate> = emptySet(),
        val error: Set<StatusCreate> = emptySet()
    )

    private val _queue = MutableStateFlow(PublishQueue())

    override val state: StateFlow<StatusPoster.State> = _queue
        .map { queue ->
            when {
                queue.posting.isNotEmpty() -> StatusPoster.State.Posting
                queue.error.isNotEmpty() -> StatusPoster.State.Error
                else -> StatusPoster.State.Idle
            }
        }
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(),
            StatusPoster.State.Idle
        )

    init {
        scope.launch(Dispatchers.IO) {
            _queue.collect { state ->
                val nextStatusToPost = state.posting.firstOrNull()
                if (nextStatusToPost != null) {
                    try {
                        clientProvider.mastodonClient.value
                            ?.statuses
                            ?.postStatus(nextStatusToPost)

                        _queue.emit(
                            state.copy(
                                posting = state.posting - nextStatusToPost
                            )
                        )
                    } catch (e: Exception) {
                        _queue.emit(
                            state.copy(
                                posting = state.posting - nextStatusToPost,
                                error = state.error + nextStatusToPost
                            )
                        )
                    }
                }
            }
        }
    }

    override fun enqueueStatus(statusCreate: StatusCreate) {
        val currentState = _queue.value
        _queue.tryEmit(
            currentState.copy(
                posting = currentState.posting + statusCreate
            )
        )
    }

    override fun retryAll() {
        val currentState = _queue.value
        _queue.tryEmit(
            currentState.copy(
                error = emptySet(),
                posting = currentState.posting + currentState.error
            )
        )
    }
}
