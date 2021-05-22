package fr.outadoc.woolly.common.feature.publictimeline.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.viewmodel.PublicTimelineViewModel
import fr.outadoc.woolly.common.feature.status.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun PublicTimelineScreen(
    insets: PaddingValues,
    currentSubScreen: PublicTimelineSubScreen,
    localListState: LazyListState,
    globalListState: LazyListState
) {
    val di = LocalDI.current
    val vm by di.instance<PublicTimelineViewModel>()

    when (currentSubScreen) {
        is PublicTimelineSubScreen.Local -> Timeline(
            insets = insets,
            statusFlow = vm.localPagingItems,
            lazyListState = localListState,
            onStatusAction = vm::onLocalStatusAction
        )
        is PublicTimelineSubScreen.Global -> Timeline(
            insets = insets,
            statusFlow = vm.globalPagingItems,
            lazyListState = globalListState,
            onStatusAction = vm::onGlobalStatusAction
        )
    }
}
