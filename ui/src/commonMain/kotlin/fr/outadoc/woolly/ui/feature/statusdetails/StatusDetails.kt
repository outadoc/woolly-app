package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.feature.status.*
import fr.outadoc.woolly.ui.richtext.RichText

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
            StatusBoostedByMention(
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
            AutomatedLabel(
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
            StatusActions(
                status = status,
                onStatusAction = onStatusAction,
                onStatusReplyClick = onStatusReplyClick
            )
        }
    }
}

@Composable
fun StatusFooter(
    modifier: Modifier = Modifier,
    status: Status
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusVisibilityIcon(
            modifier = Modifier.size(16.dp),
            visibility = status.visibility
        )

        AbsoluteTime(
            modifier = Modifier
                .alignByBaseline()
                .clickable { status.url?.let { uriHandler.openUri(it) } },
            time = status.createdAt,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle2
        )

        status.application?.let { app ->
            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .clickable { app.website?.let { uriHandler.openUri(it) } },
                text = app.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status,
    onAccountClick: (Account) -> Unit = {}
) {
    Row(modifier = modifier) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account,
            onClick = { onAccountClick(status.account) }
        )

        Column(modifier = Modifier) {
            RichText(
                modifier = Modifier.padding(end = 8.dp),
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                emojis = status.account.emojis
            )

            if (status.account.displayName.isNotBlank()) {
                Text(
                    text = "@${status.account.acct}",
                    style = MaterialTheme.typography.subtitle2,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
