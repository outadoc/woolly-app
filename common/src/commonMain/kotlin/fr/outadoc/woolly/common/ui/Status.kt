package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.htmltext.HtmlText
import kotlinx.datetime.Instant

@Composable
fun StatusPlaceholder() {
    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
fun StatusOrBoost(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
) {
    val original = status.boostedStatus ?: status
    val boostedBy = if (status.boostedStatus != null) status.account else null
    StatusBodyWithPicture(modifier, original, boostedBy, currentTime)
}

@Composable
fun StatusBodyWithPicture(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant
) {
    Row(
        modifier = modifier.padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    ) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account
        )

        StatusBodyWithActions(
            status = status,
            boostedBy = boostedBy,
            currentTime = currentTime
        )
    }
}

@Composable
fun StatusBodyWithActions(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant
) {
    Column(modifier = modifier) {
        StatusBody(
            status = status,
            boostedBy = boostedBy,
            currentTime = currentTime
        )

        StatusActions(
            modifier = Modifier.offset(x = (-16).dp),
            status = status
        ) { action ->

        }
    }
}

@Composable
fun StatusBody(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StatusHeader(
            modifier = Modifier.padding(bottom = 8.dp),
            status = status,
            currentTime = currentTime
        )

        SelectionContainer {
            HtmlText(
                html = status.content,
                style = MaterialTheme.typography.body2
            )
        }

        StatusFooter(
            modifier = Modifier.padding(top = 8.dp),
            boostedBy = boostedBy
        )
    }
}

@Composable
fun StatusFooter(modifier: Modifier = Modifier, boostedBy: Account?) {
    Column(modifier = modifier) {
        boostedBy?.let { boostedBy ->
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
}

@Composable
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
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

            RelativeTime(
                modifier = Modifier.alignByBaseline(),
                currentTime = currentTime,
                time = status.createdAt,
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1
            )
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
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        StatusAction(
            icon = Icons.Default.Reply,
            checked = false,
            contentDescription = "Reply",
            counter = status.repliesCount,
            onCheckedChange = {
                onStatusAction(StatusAction.Reply)
            }
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
                    if (boosted) StatusAction.Boost
                    else StatusAction.UndoBoost
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
                    if (favourited) StatusAction.Favourite
                    else StatusAction.UndoFavourite
                )
            }
        )
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
