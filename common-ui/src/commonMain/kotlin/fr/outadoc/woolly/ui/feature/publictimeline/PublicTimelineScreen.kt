package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.viewmodel.PublicTimelineViewModel
import fr.outadoc.woolly.ui.feature.status.StatusList
import org.kodein.di.compose.instance

@Composable
fun PublicTimelineScreen(
    insets: PaddingValues = PaddingValues(),
    currentSubScreen: PublicTimelineSubScreen,
    localListState: LazyListState,
    globalListState: LazyListState,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val viewModel by instance<PublicTimelineViewModel>()
    when (currentSubScreen) {
        PublicTimelineSubScreen.Local -> StatusList(
            insets = insets,
            statusFlow = viewModel.localPagingItems,
            lazyListState = localListState,
            onStatusAction = viewModel::onLocalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick
        )
        PublicTimelineSubScreen.Global -> StatusList(
            insets = insets,
            statusFlow = viewModel.globalPagingItems,
            lazyListState = globalListState,
            onStatusAction = viewModel::onGlobalStatusAction,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick
        )
    }
}
