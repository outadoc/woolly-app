package fr.outadoc.woolly.ui.richtext

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import fr.outadoc.mastodonk.api.entity.Emoji
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun InlineEmoji(
    modifier: Modifier = Modifier,
    emoji: Emoji
) {
    KamelImage(
        modifier = modifier,
        resource = asyncPainterResource(emoji.staticUrl),
        onLoading = { Box(modifier = modifier) },
        onFailure = { Box(modifier = modifier) },
        contentDescription = emoji.shortCode,
        animationSpec = tween(),
        contentScale = ContentScale.FillWidth
    )
}
