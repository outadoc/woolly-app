package fr.outadoc.woolly.ui.feature.status

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun StatusBodyPlain(
    modifier: Modifier = Modifier,
    status: Status,
    style: TextStyle = MaterialTheme.typography.body2
) {
    RichText(
        modifier = modifier,
        text = status.content,
        style = style,
        emojis = status.emojis
    )
}
