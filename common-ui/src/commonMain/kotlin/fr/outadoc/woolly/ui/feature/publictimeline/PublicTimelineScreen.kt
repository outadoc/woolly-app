package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.viewmodel.PublicTimelineViewModel
import fr.outadoc.woolly.ui.feature.status.StatusList
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun PublicTimelineScreen(
    insets: PaddingValues,
    currentSubScreen: PublicTimelineSubScreen,
    localListState: LazyListState,
    globalListState: LazyListState,
    onStatusClick: (Status) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<PublicTimelineViewModel>()

    when (currentSubScreen) {
        PublicTimelineSubScreen.Local -> StatusList(
            insets = insets,
            statusFlow = vm.localPagingItems,
            lazyListState = localListState,
            onStatusAction = vm::onLocalStatusAction,
            onStatusClick = onStatusClick
        )
        PublicTimelineSubScreen.Global -> StatusList(
            insets = insets,
            statusFlow = vm.globalPagingItems,
            lazyListState = globalListState,
            onStatusAction = vm::onGlobalStatusAction,
            onStatusClick = onStatusClick
        )
    }
}
