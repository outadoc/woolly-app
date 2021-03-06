package fr.outadoc.woolly.ui.feature.media

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinchToZoom(
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    onLongClickLabel: String,
    content: @Composable () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var translation by remember { mutableStateOf(Offset(0f, 0f)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {},
                onLongClick = onLongClick,
                onLongClickLabel = onLongClickLabel,
                onDoubleClick = {
                    scale = if (scale == 2f) 1f else 2f
                }
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = max(scale * zoom, 1f)
                    translation += pan
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = translation.x,
                    translationY = translation.y
                )
        ) {
            content()
        }
    }
}
