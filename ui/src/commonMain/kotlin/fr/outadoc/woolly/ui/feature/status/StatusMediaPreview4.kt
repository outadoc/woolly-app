package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.mastodonk.api.entity.Attachment

@Composable
fun StatusMediaPreview4(
    media: List<Attachment>,
    isSensitive: Boolean,
    onAttachmentClick: (Attachment) -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(bottom = StatusMediaDefaults.ImageGridPadding)
                .aspectRatio(StatusMediaDefaults.DefaultImageRatio * 2),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(end = StatusMediaDefaults.ImageGridPadding),
                media = media[0],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(start = StatusMediaDefaults.ImageGridPadding),
                media = media[1],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }

        Row(
            modifier = Modifier
                .padding(top = StatusMediaDefaults.ImageGridPadding)
                .aspectRatio(StatusMediaDefaults.DefaultImageRatio * 2),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(end = StatusMediaDefaults.ImageGridPadding),
                media = media[2],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )

            StatusMediaPreview(
                modifier = Modifier
                    .aspectRatio(StatusMediaDefaults.DefaultImageRatio)
                    .padding(start = StatusMediaDefaults.ImageGridPadding),
                media = media[3],
                isSensitive = isSensitive,
                onAttachmentClick = onAttachmentClick
            )
        }
    }
}
