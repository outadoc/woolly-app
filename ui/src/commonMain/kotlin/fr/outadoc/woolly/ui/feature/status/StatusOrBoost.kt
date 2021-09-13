package fr.outadoc.woolly.ui.feature.status

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction

@Composable
fun StatusOrBoost(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    val original = status.boostedStatus ?: status
    val boostedBy = if (status.boostedStatus != null) status.account else null

    Status(
        modifier = modifier,
        status = original,
        boostedBy = boostedBy,
        onStatusAction = onStatusAction,
        onAttachmentClick = onAttachmentClick,
        onStatusReplyClick = onStatusReplyClick,
        onAccountClick = onAccountClick
    )
}
