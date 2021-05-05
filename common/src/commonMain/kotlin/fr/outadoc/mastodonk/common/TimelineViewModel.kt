package fr.outadoc.mastodonk.common

import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(
    scope: CoroutineScope,
    private val mastodonClient: MastodonClient
) {
    val state = MutableStateFlow<ListState>(ListState.Loading)

    init {
        scope.launch(Dispatchers.IO) {
            val nextState = ListState.Content(
                mastodonClient.timelines.getPublicTimeline()
            )
            state.emit(nextState)
        }
    }
}