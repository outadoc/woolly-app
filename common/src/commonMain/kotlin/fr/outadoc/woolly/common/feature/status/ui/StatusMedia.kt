package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.AttachmentType
import fr.outadoc.woolly.common.ui.BlurHashImage
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatusMediaGrid(
    modifier: Modifier = Modifier,
    media: List<Attachment>,
    isSensitive: Boolean
) {
    val displayableMedia = media
        .filter { it.previewUrl != null }
        .take(4)

    Box(modifier = modifier.fillMaxWidth()) {
        when (displayableMedia.size) {
            1 -> SingleMediaPreview(
                media = displayableMedia.first(),
                isSensitive = isSensitive
            )
            2 -> TwoMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive
            )
            3 -> ThreeMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive
            )
            4 -> FourMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive
            )
        }
    }
}

@Composable
fun SingleMediaPreview(media: Attachment, isSensitive: Boolean) {
    StatusMediaPreview(
        modifier = Modifier.aspectRatio(16 / 9f),
        media = media,
        isSensitive = isSensitive
    )
}

@Composable
fun TwoMediaPreview(media: List<Attachment>, isSensitive: Boolean) {
    Row(
        modifier = Modifier.aspectRatio(16 / 9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(16 / 18f)
                .padding(end = 4.dp),
            media = media[0],
            isSensitive = isSensitive
        )

        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(16 / 18f)
                .padding(start = 4.dp),
            media = media[1],
            isSensitive = isSensitive
        )
    }
}

@Composable
fun ThreeMediaPreview(media: List<Attachment>, isSensitive: Boolean) {
    Row(
        modifier = Modifier.aspectRatio(16 / 9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(16 / 18f)
                .padding(end = 4.dp),
            media = media[0],
            isSensitive = isSensitive
        )

        Column(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(bottom = 2.dp),
                media = media[1],
                isSensitive = isSensitive
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(top = 2.dp),
                media = media[2],
                isSensitive = isSensitive
            )
        }
    }
}

@Composable
fun FourMediaPreview(media: List<Attachment>, isSensitive: Boolean) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .aspectRatio(32 / 9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(end = 4.dp),
                media = media[0],
                isSensitive = isSensitive
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(start = 4.dp),
                media = media[1],
                isSensitive = isSensitive
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .aspectRatio(32 / 9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(end = 4.dp),
                media = media[2],
                isSensitive = isSensitive
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(16 / 9f)
                    .padding(start = 4.dp),
                media = media[3],
                isSensitive = isSensitive
            )
        }
    }
}

@Composable
fun StatusMediaPreview(
    modifier: Modifier = Modifier,
    media: Attachment,
    isSensitive: Boolean
) {
    val uriHandler = LocalUriHandler.current
    val icon = when (media.type) {
        AttachmentType.Image -> null
        AttachmentType.Gifv -> Icons.Default.Gif
        AttachmentType.Video -> Icons.Default.PlayCircle
        AttachmentType.Audio -> Icons.Default.Mic
        AttachmentType.Unknown -> Icons.Default.Help
    }

    StatusMediaOverlay(
        icon = icon,
        contentDescription = media.description
    ) {
        StatusImage(
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { uriHandler.openUri(media.url) },
            previewUrl = media.previewUrl ?: media.url,
            contentDescription = media.description,
            blurHash = media.blurHash,
            isSensitive = isSensitive
        )
    }
}

@Composable
fun StatusMediaOverlay(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    contentDescription: String?,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        if (icon != null) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun StatusImage(
    modifier: Modifier = Modifier,
    previewUrl: String,
    contentDescription: String?,
    blurHash: String?,
    isSensitive: Boolean
) {
    if (!isSensitive) {
        KamelImage(
            modifier = modifier,
            resource = lazyImageResource(previewUrl) {
                dispatcher = Dispatchers.IO
            },
            onLoading = {
                BlurHashImage(
                    modifier = modifier,
                    blurHash = blurHash,
                    contentDescription = contentDescription
                )
            },
            contentDescription = contentDescription,
            crossfade = true,
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
