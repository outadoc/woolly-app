package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Notification
import fr.outadoc.mastodonk.api.entity.NotificationType
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.feature.status.PastRelativeTime
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun NotificationHeader(
    modifier: Modifier = Modifier,
    notification: Notification,
    startPadding: Dp,
    onAccountClick: (Account) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.width(startPadding),
                horizontalAlignment = Alignment.End
            ) {
                NotificationIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    notification = notification
                )
            }

            Column(
                modifier = Modifier.weight(0.1f, fill = true),
                horizontalAlignment = Alignment.Start
            ) {
                ProfilePicture(
                    modifier = Modifier.size(32.dp),
                    account = notification.account,
                    onClick = { onAccountClick(notification.account) }
                )
            }

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                PastRelativeTime(
                    time = notification.createdAt,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }

        val accountTitle = notification.account.displayNameOrAcct
        Text(
            modifier = Modifier.padding(
                start = startPadding,
                top = 8.dp
            ),
            text = when (notification.type) {
                NotificationType.Follow -> stringResource(
                    MR.strings.notifications_follow_title,
                    accountTitle
                )
                NotificationType.FollowRequest -> stringResource(
                    MR.strings.notifications_followRequest_title,
                    accountTitle
                )
                NotificationType.Mention -> stringResource(
                    MR.strings.notifications_mention_title
                )
                NotificationType.Boost -> stringResource(
                    MR.strings.notifications_boost_title,
                    accountTitle
                )
                NotificationType.Favourite -> stringResource(
                    MR.strings.notifications_favourite_title,
                    accountTitle
                )
                NotificationType.Poll -> stringResource(
                    MR.strings.notifications_poll_title
                )
                NotificationType.Status -> stringResource(
                    MR.strings.notifications_status_title
                )
            },
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )
    }
}
