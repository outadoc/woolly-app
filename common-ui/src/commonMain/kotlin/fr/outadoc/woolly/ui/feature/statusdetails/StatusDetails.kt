package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.feature.status.StatusBodyPlain
import fr.outadoc.woolly.ui.feature.status.StatusBoostedByMention
import fr.outadoc.woolly.ui.feature.status.StatusMediaGrid
import fr.outadoc.woolly.ui.feature.status.StatusPoll
import fr.outadoc.woolly.ui.feature.status.StatusVisibilityIcon
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun StatusDetails(
    modifier: Modifier = Modifier,
    statusOrBoost: Status,
    onStatusAction: ((StatusAction) -> Unit)? = null
) {
    val status = statusOrBoost.boostedStatus ?: statusOrBoost
    val boostedBy = if (statusOrBoost.boostedStatus != null) statusOrBoost.account else null

    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        boostedBy?.let { boostedBy ->
            StatusBoostedByMention(
                modifier = Modifier.padding(bottom = 16.dp),
                boostedBy = boostedBy
            )
        }

        StatusHeader(
            modifier = Modifier.padding(bottom = 16.dp),
            status = status
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
                isSensitive = status.isSensitive
            )
        }

        CompositionLocalProvider(
            LocalContentAlpha provides 0.7f
        ) {
            StatusFooter(
                modifier = Modifier.padding(vertical = 8.dp),
                status = status
            )
        }

        onStatusAction?.let {
            StatusActions(
                modifier = Modifier,
                status = status,
                onStatusAction = onStatusAction
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
    status: Status
) {
    val uriHandler = LocalUriHandler.current

    Row(modifier = modifier) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account,
            onClick = { uriHandler.openUri(status.account.url) }
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
