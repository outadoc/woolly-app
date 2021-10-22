package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.*
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.status.Status
import fr.outadoc.woolly.ui.feature.status.StatusBody
import fr.outadoc.woolly.ui.feature.status.StatusMediaGrid

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    notification: Notification,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable {
                when (val status = notification.status) {
                    null -> onAccountClick(notification.account)
                    else -> onStatusClick(status)
                }
            }
            .padding(16.dp)
    ) {
        val status = notification.status
        when {
            notification.type == NotificationType.Mention && status != null -> {
                Status(
                    modifier = modifier,
                    status = status,
                    onAccountClick = onAccountClick
                )
            }
            else -> {
                val startPadding = WoollyDefaults.AvatarSize + 16.dp

                NotificationHeader(
                    modifier = Modifier.padding(
                        bottom = if (notification.status != null) 4.dp else 0.dp
                    ),
                    notification = notification,
                    startPadding = startPadding,
                    onAccountClick = onAccountClick
                )

                if (status != null) {
                    if (status.content.isNotBlank()) {
                        StatusBody(
                            modifier = Modifier.padding(start = startPadding),
                            status = status
                        )
                    }

                    if (status.mediaAttachments.isNotEmpty()) {
                        StatusMediaGrid(
                            modifier = Modifier
                                .padding(
                                    start = startPadding,
                                    top = if (status.content.isNotBlank()) 8.dp else 0.dp
                                )
                                .width(256.dp),
                            media = status.mediaAttachments,
                            isSensitive = status.isSensitive,
                            onAttachmentClick = onAttachmentClick
                        )
                    }
                }
            }
        }
    }
}
