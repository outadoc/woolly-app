package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent

@Composable
fun NotificationsScreen(
    component: NotificationsComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    onLoadError: (Throwable, () -> Unit) -> Unit = { _, _ -> }
) {
    val state by component.state.collectAsState()
    NotificationList(
        insets = insets,
        notificationFlow = state.pagingData,
        lazyListState = state.listState,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick,
        onAccountClick = onAccountClick,
        onLoadError = onLoadError
    )
}
