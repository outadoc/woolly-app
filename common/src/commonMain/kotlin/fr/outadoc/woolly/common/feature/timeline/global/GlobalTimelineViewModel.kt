package fr.outadoc.woolly.common.feature.timeline.global

import fr.outadoc.woolly.common.feature.timeline.AnnotatedStatus
import fr.outadoc.woolly.common.feature.timeline.PageState
import fr.outadoc.woolly.common.feature.timeline.usecase.GetStatusListUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class GlobalTimelineViewModel(private val getStatusListUseCase: GetStatusListUseCase) {

    val state = MutableStateFlow<PageState<List<AnnotatedStatus>>>(PageState.Loading())

    suspend fun onPageOpen() {
        val statuses = getStatusListUseCase()
        state.emit(
            PageState.Content(statuses)
        )
    }
}
