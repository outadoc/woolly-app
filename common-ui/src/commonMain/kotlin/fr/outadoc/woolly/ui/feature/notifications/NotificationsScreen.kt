package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent

@Composable
fun NotificationsScreen(
    component: NotificationsComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    NotificationList(
        insets = insets,
        notificationFlow = component.pagingData,
        lazyListState = component.listState,
        onStatusClick = onStatusClick,
        onAttachmentClick = onAttachmentClick
    )
}
