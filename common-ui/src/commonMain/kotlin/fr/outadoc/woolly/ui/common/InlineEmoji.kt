package fr.outadoc.woolly.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import fr.outadoc.mastodonk.api.entity.Emoji
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun InlineEmoji(emoji: Emoji) {
    KamelImage(
        resource = lazyPainterResource(emoji.staticUrl),
        onLoading = { Box(modifier = Modifier.fillMaxSize()) },
        onFailure = { Box(modifier = Modifier.fillMaxSize()) },
        contentDescription = emoji.shortCode,
        crossfade = true,
        animationSpec = tween(),
        contentScale = ContentScale.Fit
    )
}
