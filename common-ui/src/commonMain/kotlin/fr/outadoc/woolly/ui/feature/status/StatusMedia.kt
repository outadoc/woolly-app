package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.woolly.ui.common.BlurHashImage
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.feature.media.ImageAttachment
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private const val MaxMediaCount = 4
private const val DefaultImageRatio = 16 / 9f
private val ImageGridPadding = 4.dp

@Composable
fun StatusMediaGrid(
    modifier: Modifier = Modifier,
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val displayableMedia = media
        .filter { it.previewUrl != null }
        .take(MaxMediaCount)

    Box(modifier = modifier.fillMaxWidth()) {
        when (displayableMedia.size) {
            1 -> SingleMediaPreview(
                media = displayableMedia.first(),
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            2 -> TwoMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            3 -> ThreeMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            4 -> FourMediaPreview(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}

@Composable
fun SingleMediaPreview(
    media: Attachment,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val aspectRatio = when (media) {
        is Attachment.Image -> media.meta.small?.aspect
        is Attachment.Video -> media.meta.small?.aspect
        is Attachment.Gifv -> media.meta.small?.aspect
        else -> null
    }?.toFloat() ?: DefaultImageRatio

    StatusMediaPreview(
        modifier = Modifier.aspectRatio(aspectRatio),
        media = media,
        isSensitive = isSensitive,
        onAttachmentClick = onAttachmentClick
    )
}

@Composable
fun TwoMediaPreview(
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    Row(
        modifier = Modifier.aspectRatio(DefaultImageRatio),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(DefaultImageRatio / 2)
                .padding(end = ImageGridPadding),
            media = media[0],
            isSensitive = isSensitive,
            onAttachmentClick = onAttachmentClick
        )

        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(DefaultImageRatio / 2)
                .padding(start = ImageGridPadding),
            media = media[1],
            isSensitive = isSensitive,
            onAttachmentClick = onAttachmentClick
        )
    }
}

@Composable
fun ThreeMediaPreview(
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    Row(
        modifier = Modifier.aspectRatio(DefaultImageRatio),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(DefaultImageRatio / 2)
                .padding(end = ImageGridPadding),
            media = media[0],
            isSensitive = isSensitive,
            onAttachmentClick = onAttachmentClick
        )

        Column(
            modifier = Modifier
                .padding(start = ImageGridPadding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(bottom = ImageGridPadding / 2),
                media = media[1],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(top = ImageGridPadding / 2),
                media = media[2],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}

@Composable
fun FourMediaPreview(
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(bottom = ImageGridPadding)
                .aspectRatio(DefaultImageRatio * 2),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(end = ImageGridPadding),
                media = media[0],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(start = ImageGridPadding),
                media = media[1],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }

        Row(
            modifier = Modifier
                .padding(top = ImageGridPadding)
                .aspectRatio(DefaultImageRatio * 2),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(end = ImageGridPadding),
                media = media[2],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(DefaultImageRatio)
                    .padding(start = ImageGridPadding),
                media = media[3],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}

@Composable
fun StatusMediaPreview(
    modifier: Modifier = Modifier,
    media: Attachment,
    isSensitive: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val icon = when (media) {
        is Attachment.Image ->
            if (isSensitive) Icons.Default.Preview
            else null
        is Attachment.Video -> Icons.Default.PlayCircle
        is Attachment.Gifv -> Icons.Default.Gif
        is Attachment.Audio -> Icons.Default.Mic
        is Attachment.Unknown -> Icons.Default.Help
    }

    StatusMediaOverlay(
        modifier = Modifier
            .clickable(
                role = Role.Image,
                onClickLabel = "View media source"
            ) {
              onAttachmentClick(media)
            },
        icon = icon,
        contentDescription = media.description
    ) {
        StatusImage(
            modifier = modifier
                .shadow(1.dp, shape, clip = false)
                .clip(shape),
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
                shape = WoollyTheme.AvatarShape,
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
            resource = lazyPainterResource(previewUrl),
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
            crossfade = true,
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
