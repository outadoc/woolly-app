package fr.outadoc.woolly.common.feature.globaltimeline

import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.PageState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GlobalTimelineViewModel(
    scope: CoroutineScope,
    private val mastodonClient: MastodonClient
) {
    val state = MutableStateFlow<PageState<List<Status>>>(PageState.Loading())

    init {
        scope.launch(Dispatchers.IO) {
            val nextState = PageState.Content(
                mastodonClient.timelines.getPublicTimeline()
            )
            state.emit(nextState)
        }
    }
}
