package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Attachment

@Composable
fun StatusMediaPreview3(
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

        Column(
            modifier = Modifier
                .padding(start = StatusMediaDefaults.ImageGridPadding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(bottom = StatusMediaDefaults.ImageGridPadding / 2),
                media = media[1],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(top = StatusMediaDefaults.ImageGridPadding / 2),
                media = media[2],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}
