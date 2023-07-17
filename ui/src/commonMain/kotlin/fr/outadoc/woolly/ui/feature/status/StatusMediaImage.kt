package fr.outadoc.woolly.ui.feature.status

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import fr.outadoc.woolly.ui.common.BlurHashImage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun StatusMediaImage(
    modifier: Modifier = Modifier,
    previewUrl: String,
    contentDescription: String?,
    blurHash: String?,
    isSensitive: Boolean
) {
    if (!isSensitive) {
        KamelImage(
            modifier = modifier,
            resource = asyncPainterResource(previewUrl),
            onLoading = {
                BlurHashImage(
                    modifier = modifier,
                    blurHash = blurHash,
                    contentDescription = contentDescription
                )
            },
            onFailure = {
                BlurHashImage(
                    modifier = modifier,
                    blurHash = blurHash,
                    contentDescription = contentDescription
                )
            },
            contentDescription = contentDescription,
            animationSpec = tween(),
            contentScale = ContentScale.Crop
        )
    } else {
        BlurHashImage(
            modifier = modifier,
            blurHash = blurHash,
            contentDescription = contentDescription
        )
    }
}
