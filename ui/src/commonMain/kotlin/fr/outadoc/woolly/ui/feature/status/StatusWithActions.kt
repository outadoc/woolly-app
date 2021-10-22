package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.feature.card.StatusCard
import fr.outadoc.woolly.ui.feature.poll.Poll

@Composable
fun StatusWithActions(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account? = null,
    onStatusAction: ((StatusAction) -> Unit)? = null,
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier.fillMaxWidth()) {
        StatusHeader(
            modifier = Modifier.padding(bottom = 4.dp),
            status = status
        )

        if (status.content.isNotBlank()) {
            StatusBody(
                modifier = Modifier.padding(vertical = 4.dp),
                status = status
            )
        }

        status.poll?.let { poll ->
            Poll(
                modifier = Modifier.padding(vertical = 4.dp),
                poll = poll,
                onClick = { status.url?.let { uriHandler.openUri(it) } }
            )
        }

        if (status.mediaAttachments.isNotEmpty()) {
            StatusMediaGrid(
                modifier = Modifier.padding(vertical = 4.dp),
                media = status.mediaAttachments,
                isSensitive = status.isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }

        status.card?.let { card ->
            StatusCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                card = card,
                onClick = { uriHandler.openUri(card.url) }
            )
        }

        boostedBy?.let { boostedBy ->
            StatusBoostedByLabel(
                modifier = Modifier.padding(vertical = 4.dp),
                boostedBy = boostedBy,
                onAccountClick = onAccountClick
            )
        }

        onStatusAction?.let {
            StatusActionBar(
                modifier = Modifier.offset(x = (-16).dp),
                status = status,
                onStatusAction = onStatusAction,
                onStatusReplyClick = onStatusReplyClick
            )
        }
    }
}
