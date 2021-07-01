package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.ui.StatusAction
import fr.outadoc.woolly.common.ui.WoollyTheme
import fr.outadoc.woolly.htmltext.HtmlText
import kotlinx.datetime.Instant

@Composable
fun StatusPlaceholder() {
    Spacer(modifier = Modifier.height(128.dp))
}

@Composable
fun StatusOrBoost(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant,
    onStatusAction: (StatusAction) -> Unit
) {
    val original = status.boostedStatus ?: status
    val boostedBy = if (status.boostedStatus != null) status.account else null

    Status(
        modifier = modifier,
        status = original,
        boostedBy = boostedBy,
        currentTime = currentTime,
        onStatusAction = onStatusAction
    )
}

@Composable
fun Status(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account? = null,
    currentTime: Instant? = null,
    onStatusAction: ((StatusAction) -> Unit)? = null
) {
    Row(modifier = modifier) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account
        )

        StatusWithActions(
            status = status,
            boostedBy = boostedBy,
            currentTime = currentTime,
            onStatusAction = onStatusAction
        )
    }
}

@Composable
fun StatusWithActions(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant?,
    onStatusAction: ((StatusAction) -> Unit)?
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StatusHeader(
            modifier = Modifier.padding(bottom = 8.dp),
            status = status,
            currentTime = currentTime
        )

        if (status.content.isNotBlank()) {
            StatusBodyOrWarning(status = status)
        }

        if (status.mediaAttachments.isNotEmpty()) {
            StatusMediaGrid(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp
                ),
                media = status.mediaAttachments,
                isSensitive = status.isSensitive
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
                onStatusAction = onStatusAction
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatusBodyOrWarning(
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
            StatusBody(
                modifier = modifier.padding(top = 8.dp),
                status = status
            )
        }

    } else {
        StatusBody(
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Content warning",
                    modifier = Modifier.padding(end = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = contentWarning,
                    style = MaterialTheme.typography.body1,
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
fun StatusBody(
    modifier: Modifier = Modifier,
    status: Status
) {
    HtmlText(
        modifier = modifier,
        html = status.content,
        style = MaterialTheme.typography.body2
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
    status: Status,
    currentTime: Instant?
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .fillMaxWidth(0.8f),
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            currentTime?.let {
                RelativeTime(
                    modifier = Modifier.alignByBaseline(),
                    currentTime = currentTime,
                    time = status.createdAt,
                    style = MaterialTheme.typography.subtitle2,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    maxLines = 1
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
fun StatusActions(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit
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
                onCheckedChange = {}
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
