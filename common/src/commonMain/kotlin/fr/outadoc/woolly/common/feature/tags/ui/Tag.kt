package fr.outadoc.woolly.common.feature.tags.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.ui.WoollyListItem

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
        title = {
            Column {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.body1
                )

                val lastDayUsage = tag.history
                    ?.maxBy { it.day }
                    ?.accountCount

                if (lastDayUsage != null) {
                    Text(
                        text = "$lastDayUsage people talking",
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
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
        title = {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.body1
            )
        },
        onClick = onClick
    )
}
