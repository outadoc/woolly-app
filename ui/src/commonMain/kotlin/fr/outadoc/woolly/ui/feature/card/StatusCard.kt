package fr.outadoc.woolly.ui.feature.card

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Card

@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    card: Card,
    onClick: () -> Unit = {}
) {
    when (card) {
        is Card.Video -> MediaCard(
            modifier = modifier,
            card = card,
            onClick = onClick
        )
        is Card.Photo -> MediaCard(
            modifier = modifier,
            card = card,
            onClick = onClick
        )
        is Card.Link -> LinkCard(
            modifier = modifier,
            card = card,
            onClick = onClick,
            defaultIcon = {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = "Link preview"
                )
            }
        )
        is Card.Rich -> {}
    }
}
