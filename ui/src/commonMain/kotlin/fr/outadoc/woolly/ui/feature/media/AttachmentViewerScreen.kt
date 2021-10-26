package fr.outadoc.woolly.ui.feature.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import fr.outadoc.woolly.common.feature.media.ImageAttachment
import fr.outadoc.woolly.common.feature.media.component.AttachmentViewerComponent
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.feature.error.ErrorScreen
import fr.outadoc.woolly.ui.strings.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun ImageViewerScreen(
    component: AttachmentViewerComponent,
    image: ImageAttachment
) {
    val uriHandler = LocalUriHandler.current

    PinchToZoom(
        modifier = Modifier.fillMaxSize(),
        onLongClick = { uriHandler.openUri(image.url) },
        onLongClickLabel = stringResource(MR.strings.status_mediaPreview_cd)
    ) {
        KamelImage(
            modifier = Modifier.fillMaxSize(),
            resource = lazyPainterResource(image.url),
            contentDescription = image.contentDescription,
            contentScale = ContentScale.Fit,
            crossfade = true,
            onLoading = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            },
            onFailure = { e ->
                ErrorScreen(error = e)
            }
        )
    }
}
