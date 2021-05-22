package fr.outadoc.woolly.common.feature.home.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.home.viewmodel.HomeTimelineViewModel
import fr.outadoc.woolly.common.feature.status.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun HomeTimelineScreen(
    insets: PaddingValues,
    listState: LazyListState
) {
    val di = LocalDI.current
    val vm by di.instance<HomeTimelineViewModel>()

    Timeline(
        insets = insets,
        statusFlow = vm.homePagingItems,
        lazyListState = listState,
        onStatusAction = vm::onStatusAction
    )
}
