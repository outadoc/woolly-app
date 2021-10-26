package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.notifications.NotificationsSubScreen
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun NotificationsSubScreen.getTitle() = when (this) {
    NotificationsSubScreen.All -> stringResource(MR.strings.notifications_all_title)
    NotificationsSubScreen.MentionsOnly -> stringResource(MR.strings.notifications_mentions_title)
}
