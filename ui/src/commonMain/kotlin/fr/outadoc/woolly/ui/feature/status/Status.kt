package fr.outadoc.woolly.ui.feature.status

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.StatusVisibility
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.common.FillFirstThenWrap
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun StatusPlaceholder() {
    Spacer(modifier = Modifier.height(256.dp))
}

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
            onStatusReplyClick = onStatusReplyClick
        )
    }
}

@Composable
fun StatusWithActions(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account? = null,
    onStatusAction: ((StatusAction) -> Unit)? = null,
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier.fillMaxWidth()) {
        StatusHeader(
            modifier = Modifier.padding(bottom = 8.dp),
            status = status
        )

        if (status.content.isNotBlank()) {
            StatusBody(status = status)
        }

        status.poll?.let { poll ->
            StatusPoll(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                status.url?.let { uriHandler.openUri(it) }
            }
        }

        if (status.mediaAttachments.isNotEmpty()) {
            StatusMediaGrid(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp
                ),
                media = status.mediaAttachments,
                isSensitive = status.isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }

        boostedBy?.let { boostedBy ->
            StatusBoostedByMention(
                modifier = Modifier.padding(top = 8.dp),
                boostedBy = boostedBy
            )
        }

        onStatusAction?.let {
            StatusActions(
                modifier = Modifier.offset(x = (-16).dp),
                status = status,
                onStatusAction = onStatusAction,
                onStatusReplyClick = onStatusReplyClick
            )
        }
    }
}

@Composable
fun StatusPoll(
    modifier: Modifier = Modifier,
    onClickPlaceholder: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClickPlaceholder
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Poll,
                    contentDescription = "View poll",
                    modifier = Modifier.padding(end = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = "View poll",
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatusBody(
    modifier: Modifier = Modifier,
    status: Status
) {
    if (status.contentWarningText.isNotBlank()) {
        var isCollapsed by remember { mutableStateOf(true) }

        ContentWarningBanner(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            contentWarning = status.contentWarningText,
            isCollapsed = isCollapsed,
            onToggle = {
                isCollapsed = !isCollapsed
            }
        )

        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {
            StatusBodyPlain(
                modifier = modifier.padding(top = 8.dp),
                status = status
            )
        }

    } else {
        StatusBodyPlain(
            modifier = modifier,
            status = status
        )
    }
}

@Composable
fun ContentWarningBanner(
    modifier: Modifier = Modifier,
    contentWarning: String,
    isCollapsed: Boolean,
    onToggle: () -> Unit
) {
    Card(modifier = modifier.clickable { onToggle() }) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Content warning",
                    modifier = Modifier.padding(end = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = contentWarning,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isCollapsed) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Expand post"
                )
            } else {
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = "Collapse post"
                )
            }
        }
    }
}

@Composable
fun StatusBodyPlain(
    modifier: Modifier = Modifier,
    status: Status,
    style: TextStyle = MaterialTheme.typography.body2
) {
    RichText(
        modifier = modifier,
        text = status.content,
        style = style,
        emojis = status.emojis
    )
}

@Composable
fun StatusBoostedByMention(modifier: Modifier = Modifier, boostedBy: Account) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
                imageVector = Icons.Default.Repeat,
                contentDescription = "Boosted",
                tint = LocalContentColor.current.copy(alpha = 0.7f)
            )

            Text(
                "${boostedBy.displayNameOrAcct} boosted",
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = FillFirstThenWrap,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichText(
                modifier = Modifier
                    .alignByBaseline()
                    .weight(1f, fill = false)
                    .padding(end = 8.dp),
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                emojis = status.account.emojis
            )

            CompositionLocalProvider(
                LocalContentAlpha provides 0.7f
            ) {
                StatusVisibilityIcon(
                    modifier = Modifier.size(16.dp),
                    visibility = status.visibility
                )

                RelativeTime(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .alignByBaseline(),
                    time = status.createdAt,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }

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

@Composable
fun StatusVisibilityIcon(
    modifier: Modifier = Modifier,
    visibility: StatusVisibility
) {
    when (visibility) {
        StatusVisibility.Public -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Public,
            contentDescription = "Public"
        )
        StatusVisibility.Unlisted -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.LockOpen,
            contentDescription = "Unlisted"
        )
        StatusVisibility.Private -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Lock,
            contentDescription = "Followers-only"
        )
        StatusVisibility.Direct -> Icon(
            modifier = modifier,
            imageVector = Icons.Default.Mail,
            contentDescription = "Direct"
        )
    }
}

@Composable
fun StatusActions(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit,
    onStatusReplyClick: (Status) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.requiredWidth(min(maxWidth, 250.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusAction(
                icon = Icons.Default.Reply,
                checked = false,
                contentDescription = "Reply",
                counter = status.repliesCount,
                onCheckedChange = { onStatusReplyClick(status) }
            )

            StatusAction(
                modifier = Modifier.padding(start = 16.dp),
                checked = status.isBoosted == true,
                checkedColor = WoollyTheme.BoostColor,
                icon = Icons.Default.Repeat,
                contentDescription = if (status.isBoosted == true) "Undo boost" else "Boost",
                counter = status.boostsCount,
                onCheckedChange = { boosted ->
                    onStatusAction(
                        if (boosted) StatusAction.Boost(status)
                        else StatusAction.UndoBoost(status)
                    )
                }
            )

            StatusAction(
                modifier = Modifier.padding(start = 16.dp),
                checked = status.isFavourited == true,
                checkedColor = WoollyTheme.FavouriteColor,
                icon = Icons.Default.Star,
                contentDescription = if (status.isBoosted == true) "Remove from favourites" else "Add to favourites",
                counter = status.favouritesCount,
                onCheckedChange = { favourited ->
                    onStatusAction(
                        if (favourited) StatusAction.Favourite(status)
                        else StatusAction.UndoFavourite(status)
                    )
                }
            )
        }
    }
}

@Composable
private fun StatusAction(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedColor: Color = LocalContentColor.current,
    icon: ImageVector,
    contentDescription: String,
    counter: Long,
    onCheckedChange: (Boolean) -> Unit
) {
    val color =
        if (checked) checkedColor
        else LocalContentColor.current.copy(alpha = 0.7f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconToggleButton(
            modifier = modifier,
            checked = checked,
            onCheckedChange = onCheckedChange
        ) {
            Icon(
                icon,
                modifier = Modifier.size(24.dp),
                contentDescription = contentDescription,
                tint = color
            )
        }

        Text(
            text = counter.toString(),
            maxLines = 1,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
