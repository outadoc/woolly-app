package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
                modifier = Modifier.padding(bottom = 16.dp),
                boostedBy = boostedBy,
                onAccountClick = onAccountClick
            )
        }

        StatusHeader(
            modifier = Modifier.padding(bottom = 16.dp),
            status = status,
            onAccountClick = onAccountClick
        )

        if (status.content.isNotBlank()) {
            StatusBodyPlain(
                modifier = Modifier.padding(bottom = 8.dp),
                status = status,
                style = MaterialTheme.typography.body1
            )
        }

        status.poll?.let { poll ->
            StatusPoll(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                status.url?.let { uriHandler.openUri(it) }
            }
        }

        if (status.mediaAttachments.isNotEmpty()) {
            StatusMediaGrid(
                modifier = Modifier.padding(vertical = 16.dp),
                media = status.mediaAttachments,
                isSensitive = status.isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }

        if (status.account.isBot == true) {
            StatusAutomatedLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        CompositionLocalProvider(LocalContentAlpha provides 0.7f) {
            StatusFooter(
                modifier = Modifier.padding(vertical = 8.dp),
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
