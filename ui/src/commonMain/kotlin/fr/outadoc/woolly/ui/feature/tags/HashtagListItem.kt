package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.ui.common.WoollyListItem

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
