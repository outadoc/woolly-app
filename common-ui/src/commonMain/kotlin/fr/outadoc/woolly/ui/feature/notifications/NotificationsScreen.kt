package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.notifications.viewmodel.NotificationsViewModel
import org.kodein.di.compose.instance

@Composable
fun NotificationsScreen(
    insets: PaddingValues = PaddingValues(),
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val viewModel by instance<NotificationsViewModel>()
    NotificationList(
        insets = insets,
        notificationFlow = viewModel.pagingData,
        lazyListState = listState,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick
    )
}
