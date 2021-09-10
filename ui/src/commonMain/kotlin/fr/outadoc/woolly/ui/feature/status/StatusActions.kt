package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.common.feature.status.formatShort
import fr.outadoc.woolly.ui.common.WoollyTheme


@Composable
fun StatusActions(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit,
    onStatusReplyClick: (Status) -> Unit,
    maxActionsWidth: Dp = 200.dp
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.requiredWidth(min(maxWidth, maxActionsWidth)),
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
    iconSize: Dp = 22.dp,
    onCheckedChange: (Boolean) -> Unit = {}
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
                modifier = Modifier.size(iconSize),
                contentDescription = contentDescription,
                tint = color
            )
        }

        if (counter > 0) {
            Text(
                text = counter.formatShort(),
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
