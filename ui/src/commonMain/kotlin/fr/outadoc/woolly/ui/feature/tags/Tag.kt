package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.ui.common.WoollyListItem

@Composable
fun HashtagPlaceholder() {
    Spacer(modifier = Modifier.height(128.dp))
}


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

@Composable
fun HashtagListItem(
    modifier: Modifier = Modifier,
    tag: Tag,
    onClick: () -> Unit
) {
    WoollyListItem(
        modifier = modifier,
        icon = {
            Icon(
                Icons.Default.Tag,
                contentDescription = "Hashtag"
            )
        },
        title = { Text(text = tag.name) },
        onClick = onClick
    )
}
