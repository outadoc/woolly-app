package fr.outadoc.woolly.common.feature.tags.ui

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.ui.WoollyListItem

@Composable
fun TrendingTag(tag: Tag, onClick: () -> Unit) {
    WoollyListItem(
        icon = {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = "Trending"
            )
        },
        title = {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.body1
            )
        },
        onClick = onClick
    )
}
