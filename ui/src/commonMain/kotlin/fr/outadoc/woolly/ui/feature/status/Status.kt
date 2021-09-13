package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction

@Composable
fun Status(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account? = null,
    onStatusAction: ((StatusAction) -> Unit)? = null,
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    Row(modifier = modifier) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account,
            onClick = { onAccountClick(status.account) }
        )

        StatusWithActions(
            status = status,
            boostedBy = boostedBy,
            onStatusAction = onStatusAction,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
    }
}
