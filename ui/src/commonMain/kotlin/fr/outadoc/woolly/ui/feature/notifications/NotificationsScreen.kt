package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.notifications.NotificationsSubScreen
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent

@Composable
fun NotificationsScreen(
    component: NotificationsComponent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val state by component.state.collectAsState()
    when (state.subScreen) {
        NotificationsSubScreen.All -> NotificationList(
            insets = insets,
            notificationFlow = component.allPagingData,
            lazyListState = component.allListState,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onAccountClick = onAccountClick
        )

        NotificationsSubScreen.MentionsOnly -> NotificationList(
            insets = insets,
            notificationFlow = component.mentionsPagingData,
            lazyListState = component.mentionsListState,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onAccountClick = onAccountClick
        )
    }
}
