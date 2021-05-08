package fr.outadoc.woolly.common.feature.timeline.global

import androidx.compose.ui.graphics.Color
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.timeline.AnnotatedStatus
import fr.outadoc.woolly.common.feature.timeline.PageState
import fr.outadoc.woolly.common.feature.timeline.StatusAnnotator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GlobalTimelineViewModel(
    scope: CoroutineScope,
    private val mastodonClient: MastodonClient,
    private val statusAnnotator: StatusAnnotator
) {
    val state = MutableStateFlow<PageState<List<AnnotatedStatus>>>(PageState.Loading())

    init {
        scope.launch(Dispatchers.IO) {
            val res = mastodonClient.timelines.getPublicTimeline()
            withContext(Dispatchers.Default) {
                state.emit(
                    PageState.Content(
                        res.contents.map { status ->
                            statusAnnotator.annotateStatus(status, Color(0xff64B5F6))
                        }
                    )
                )
            }
        }
    }
}
