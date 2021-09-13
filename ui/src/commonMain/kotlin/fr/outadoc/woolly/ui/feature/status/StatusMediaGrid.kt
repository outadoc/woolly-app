package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Attachment

@Composable
fun StatusMediaGrid(
    modifier: Modifier = Modifier,
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    val displayableMedia = media
        .filter { it.previewUrl != null }
        .take(StatusMediaDefaults.MaxMediaCount)

    Box(modifier = modifier.fillMaxWidth()) {
        when (displayableMedia.size) {
            1 -> StatusMediaPreview1(
                media = displayableMedia.first(),
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            2 -> StatusMediaPreview2(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            3 -> StatusMediaPreview3(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
            4 -> StatusMediaPreview4(
                media = displayableMedia,
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}
