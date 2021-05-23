package fr.outadoc.woolly.htmltext

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.coroutineScope

@Composable
internal fun MaterialClickableText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    isClickableIndex: (Int) -> Boolean,
    onClick: (Int) -> Unit
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val pressIndicator = Modifier.pointerInput(onClick) {
        forEachGesture {
            coroutineScope {
                val down = awaitPointerEventScope {
                    awaitFirstDown().also {
                        layoutResult.value?.let { layoutResult ->
                            if (isClickableIndex(layoutResult.getOffsetForPosition(it.position))) {
                                it.consumeDownChange()
                            }
                        }
                    }
                }

                var up: PointerInputChange? = null
                // wait for first tap up or long press
                up = awaitPointerEventScope {
                    waitForUpOrCancellation()?.also {
                        it.consumeDownChange()
                    }
                }

                if (up != null) {
                    // tap was successful.
                    layoutResult.value?.let { layoutResult ->
                        onClick(layoutResult.getOffsetForPosition(up.position))
                    }
                }
            }
        }
    }

    Text(
        text = text,
        modifier = modifier.then(pressIndicator),
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        }
    )
}
