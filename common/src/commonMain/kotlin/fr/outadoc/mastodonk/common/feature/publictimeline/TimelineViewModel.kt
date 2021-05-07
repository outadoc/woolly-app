package fr.outadoc.mastodonk.common.feature.publictimeline

import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.Page
import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(
    scope: CoroutineScope,
    private val mastodonClient: MastodonClient
) {
    sealed class ListState {
        object Loading : ListState()
        data class Content(val page: Page<List<Status>>) : ListState()
    }

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
