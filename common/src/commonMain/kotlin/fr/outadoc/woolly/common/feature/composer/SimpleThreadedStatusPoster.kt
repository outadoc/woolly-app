package fr.outadoc.woolly.common.feature.composer

import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class SimpleThreadedStatusPoster(
    scope: CoroutineScope,
    private val clientProvider: MastodonClientProvider
) : StatusPoster {

    private val _state = MutableStateFlow(StatusPoster.State())

    override val state: StateFlow<StatusPoster.State> = _state
        .map { state ->
            val nextStatusToPost = state.posting.firstOrNull()
            if (nextStatusToPost != null) {
                try {
                    clientProvider.mastodonClient.value
                        ?.statuses
                        ?.postStatus(nextStatusToPost)

                    state.copy(posting = state.posting - nextStatusToPost)
                } catch (e: Exception) {
                    state.copy(
                        posting = state.posting - nextStatusToPost,
                        error = state.error + nextStatusToPost
                    )
                }
            } else {
                state
            }
        }
        .stateIn(
            scope,
            initialValue = StatusPoster.State(),
            started = SharingStarted.Eagerly
        )

    override suspend fun enqueueStatus(statusCreate: StatusCreate) {
        val currentState = _state.value
        _state.value = currentState.copy(
            posting = currentState.posting + statusCreate
        )
    }

    override suspend fun retryAll() {
        // TODO investigate why it's not working
        val currentState = _state.value
        _state.value = currentState.copy(
            error = emptySet(),
            posting = currentState.posting + currentState.error
        )
    }
}
