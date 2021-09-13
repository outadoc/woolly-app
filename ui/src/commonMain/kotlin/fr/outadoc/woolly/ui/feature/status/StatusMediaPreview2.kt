@file:JvmName("StatusMediaPreview1Kt")

package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Attachment

@Composable
fun StatusMediaPreview2(
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    Row(
        modifier = Modifier.aspectRatio(StatusMediaDefaults.DefaultImageRatio),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(StatusMediaDefaults.DefaultImageRatio / 2)
                .padding(end = StatusMediaDefaults.ImageGridPadding),
            media = media[0],
            isSensitive = isSensitive,
            onAttachmentClick = onAttachmentClick
        )

        StatusMediaPreview(
            modifier = Modifier
                .aspectRatio(StatusMediaDefaults.DefaultImageRatio / 2)
                .padding(start = StatusMediaDefaults.ImageGridPadding),
            media = media[1],
            isSensitive = isSensitive,
            onAttachmentClick = onAttachmentClick
        )
    }
}
