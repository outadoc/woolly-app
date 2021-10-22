package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.feature.card.StatusCard
import fr.outadoc.woolly.ui.feature.poll.Poll
import fr.outadoc.woolly.ui.feature.status.*

@Composable
fun StatusDetails(
    modifier: Modifier = Modifier,
    statusOrBoost: Status,
    onStatusAction: ((StatusAction) -> Unit)? = null,
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val status = statusOrBoost.boostedStatus ?: statusOrBoost
    val boostedBy = if (statusOrBoost.boostedStatus != null) statusOrBoost.account else null

    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        boostedBy?.let { boostedBy ->
            StatusBoostedByLabel(
                modifier = Modifier.padding(bottom = 8.dp),
                boostedBy = boostedBy,
                onAccountClick = onAccountClick
            )
        }

        StatusHeader(
            modifier = Modifier.padding(vertical = 4.dp),
            status = status,
            onAccountClick = onAccountClick
        )

        if (status.content.isNotBlank()) {
            StatusBodyPlain(
                modifier = Modifier.padding(vertical = 4.dp),
                status = status,
                style = MaterialTheme.typography.body1
            )
        }

        status.poll?.let { poll ->
            Poll(
                modifier = Modifier.padding(vertical = 4.dp),
                poll = poll
            ) {
                status.url?.let { uriHandler.openUri(it) }
            }
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

        if (status.account.isBot == true) {
            StatusAutomatedLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            StatusFooter(
                modifier = Modifier.padding(vertical = 4.dp),
                status = status
            )
        }

        onStatusAction?.let {
            StatusActionBar(
                status = status,
                onStatusAction = onStatusAction,
                onStatusReplyClick = onStatusReplyClick
            )
        }
    }
}
