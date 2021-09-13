package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Tag

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingTagListItem(
    modifier: Modifier = Modifier,
    tag: Tag,
    onClick: () -> Unit
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        icon = {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = "Trending"
            )
        },
        secondaryText = {
            val lastDayUsage = tag.history
                ?.maxByOrNull { it.day }
                ?.accountCount

            if (lastDayUsage != null) {
                Text(text = "$lastDayUsage people talking")
            }
        }
    ) {
        Text(text = tag.name)
    }
}
