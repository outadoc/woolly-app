package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.ui.common.WoollyListItem

@Composable
fun TrendingTagListItem(
    modifier: Modifier = Modifier,
    tag: Tag,
    onClick: () -> Unit
) {
    WoollyListItem(
        modifier = modifier,
        icon = {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = "Trending"
            )
        },
        title = { Text(text = tag.name) },
        secondaryText = {
            val lastDayUsage = tag.history
                ?.maxByOrNull { it.day }
                ?.accountCount

            if (lastDayUsage != null) {
                Text(text = "$lastDayUsage people talking")
            }
        },
        onClick = onClick
    )
}
