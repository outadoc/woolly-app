package fr.outadoc.woolly.ui.richtext

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    onClick: (Int) -> Unit,
    inlineContent: Map<String, InlineTextContent>
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val pressIndicator = Modifier.pointerInput(onClick) {
        forEachGesture {
            coroutineScope {
                awaitPointerEventScope {
                    // Wait for tap
                    awaitFirstDown().also { down ->
                        // Check that text has been laid out (it should be)
                        val layoutRes = layoutResult.value ?: return@also

                        // Only consume the event if we're actually clicking on a link
                        if (isClickableIndex(layoutRes.getOffsetForPosition(down.position))) {
                            // Prevent parent components from getting the event,
                            // we're dealing with it
                            down.consumeDownChange()

                            // Wait for the user to stop clicking
                            waitForUpOrCancellation()?.also { up ->
                                // Tap on a link was successful, call onClick
                                up.consumeDownChange()
                                onClick(layoutRes.getOffsetForPosition(up.position))
                            }
                        }
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
        },
        inlineContent = inlineContent
    )
}
