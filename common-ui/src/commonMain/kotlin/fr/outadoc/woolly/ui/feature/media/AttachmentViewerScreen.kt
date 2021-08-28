package fr.outadoc.woolly.ui.feature.media

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import fr.outadoc.woolly.ui.feature.status.ErrorScreen
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun ImageViewerScreen(image: ImageAttachment) {
    PinchToZoomImage(
        imageUrl = image.url,
        contentDescription = image.contentDescription
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinchToZoomImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String?
) {
    var scale by remember { mutableStateOf(1f) }
    var rotationState by remember { mutableStateOf(0f) }
    var translationX by remember { mutableStateOf(0f) }
    var translationY by remember { mutableStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {},
                onDoubleClick = {
                    scale = if (scale == 2f) 1f else 2f
                }
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    scale *= zoom
                    rotationState += rotation
                    translationX += pan.x
                    translationY += pan.y
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        KamelImage(
            modifier = Modifier.graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotationState,
                translationX = translationX,
                translationY = translationY
            ),
            resource = lazyPainterResource(imageUrl),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
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
