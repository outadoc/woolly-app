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
import fr.outadoc.woolly.ui.common.WoollyTheme

@Composable
fun NotificationIcon(modifier: Modifier = Modifier, notification: Notification) {
    Box(modifier) {
        when (notification.type) {
            NotificationType.Follow -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = "New follower",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.FollowRequest -> Icon(
                Icons.Default.PersonAdd,
                contentDescription = "New follow request",
                tint = MaterialTheme.colors.secondary
            )
            NotificationType.Mention -> Icon(
                Icons.Default.Comment,
                contentDescription = "New mention",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Boost -> Icon(
                Icons.Default.Repeat,
                contentDescription = "New boost",
                tint = WoollyTheme.BoostColor
            )
            NotificationType.Favourite -> Icon(
                Icons.Default.Star,
                contentDescription = "New favourite",
                tint = WoollyTheme.FavouriteColor
            )
            NotificationType.Poll -> Icon(
                Icons.Default.Poll,
                contentDescription = "Poll results are in",
                tint = MaterialTheme.colors.primary
            )
            NotificationType.Status -> Icon(
                Icons.Default.Inbox,
                contentDescription = "New post",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}
