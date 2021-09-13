package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Attachment

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
        StatusMediaImage(
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
