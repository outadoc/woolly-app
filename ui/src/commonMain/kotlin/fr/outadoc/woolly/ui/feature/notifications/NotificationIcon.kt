package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun NotificationIcon(modifier: Modifier = Modifier, notification: Notification) {
    Box(modifier) {
        when (notification.type) {
            NotificationType.Follow -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = stringResource(MR.strings.notifications_follow_cd),
                tint = MaterialTheme.colors.primary
            )
            NotificationType.FollowRequest -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = stringResource(MR.strings.notifications_followRequest_cd),
                tint = MaterialTheme.colors.secondary
            )
            NotificationType.Mention -> Icon(
                Icons.Default.Comment,
                contentDescription = stringResource(MR.strings.notifications_mention_cd),
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Boost -> Icon(
                Icons.Default.Repeat,
                contentDescription = stringResource(MR.strings.notifications_boost_cd),
                tint = WoollyTheme.BoostColor
            )
            NotificationType.Favourite -> Icon(
                Icons.Default.Star,
                contentDescription = stringResource(MR.strings.notifications_favourite_cd),
                tint = WoollyTheme.FavouriteColor
            )
            NotificationType.Poll -> Icon(
                Icons.Default.Poll,
                contentDescription = stringResource(MR.strings.notifications_poll_cd),
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Status -> Icon(
                Icons.Default.Inbox,
                contentDescription = stringResource(MR.strings.notifications_status_cd),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
