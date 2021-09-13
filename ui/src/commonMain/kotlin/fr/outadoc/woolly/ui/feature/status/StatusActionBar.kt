package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.common.WoollyTheme

@Composable
fun StatusActionBar(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit,
    onStatusReplyClick: (Status) -> Unit,
    actionsWidth: Dp = 64.dp
) {
    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusAction(
                modifier = Modifier.width(actionsWidth),
                icon = Icons.Default.Reply,
                checked = false,
                contentDescription = "Reply",
                counter = status.repliesCount,
                onCheckedChange = { onStatusReplyClick(status) }
            )

            StatusAction(
                modifier = Modifier.width(actionsWidth),
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
                modifier = Modifier.width(actionsWidth),
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
