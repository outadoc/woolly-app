package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Attachment

@Composable
fun StatusMediaPreview1(
    media: Attachment,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val aspectRatio = when (media) {
        is Attachment.Image -> media.meta.small?.aspect
        is Attachment.Video -> media.meta.small?.aspect
        is Attachment.Gifv -> media.meta.small?.aspect
        else -> null
    }?.toFloat() ?: StatusMediaDefaults.DefaultImageRatio

    StatusMediaPreview(
        modifier = Modifier.aspectRatio(aspectRatio),
        media = media,
        isSensitive = isSensitive,
        onAttachmentClick = onAttachmentClick
    )
}
