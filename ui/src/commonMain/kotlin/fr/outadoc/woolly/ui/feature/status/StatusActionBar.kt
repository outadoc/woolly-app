package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
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
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun StatusActionBar(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit,
    onStatusReplyClick: (Status) -> Unit,
    actionsWidth: Dp = 64.dp,
    iconSize: Dp = 22.dp
) {
    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusAction(
                modifier = Modifier.width(actionsWidth),
                checked = false,
                counter = status.repliesCount
            ) {
                IconButton(onClick = { onStatusReplyClick(status) }) {
                    Icon(
                        Icons.Default.Reply,
                        modifier = Modifier.size(iconSize),
                        contentDescription = stringResource(MR.strings.status_reply_cd)
                    )
                }
            }

            StatusAction(
                modifier = Modifier.width(actionsWidth),
                checked = status.isBoosted == true,
                checkedColor = WoollyTheme.BoostColor,
                counter = status.boostsCount
            ) {
                IconToggleButton(
                    checked = status.isBoosted == true,
                    onCheckedChange = { boosted ->
                        onStatusAction(
                            if (boosted) StatusAction.Boost(status)
                            else StatusAction.UndoBoost(status)
                        )
                    }
                ) {
                    Icon(
                        Icons.Default.Repeat,
                        modifier = Modifier.size(iconSize),
                        contentDescription = if (status.isBoosted == true) {
                            stringResource(MR.strings.status_boostUndo_action)
                        } else {
                            stringResource(MR.strings.status_boost_action)
                        }
                    )
                }
            }

            StatusAction(
                modifier = Modifier.width(actionsWidth),
                checked = status.isFavourited == true,
                checkedColor = WoollyTheme.FavouriteColor,
                counter = status.favouritesCount
            ) {
                IconToggleButton(
                    checked = status.isFavourited == true,
                    onCheckedChange = { favourited ->
                        onStatusAction(
                            if (favourited) StatusAction.Favourite(status)
                            else StatusAction.UndoFavourite(status)
                        )
                    }
                ) {
                    Icon(
                        Icons.Default.Star,
                        modifier = Modifier.size(iconSize),
                        contentDescription = if (status.isBoosted == true) {
                            stringResource(MR.strings.status_favouriteUndo_action)
                        } else {
                            stringResource(MR.strings.status_favourite_action)
                        }
                    )
                }
            }
        }
    }
}
