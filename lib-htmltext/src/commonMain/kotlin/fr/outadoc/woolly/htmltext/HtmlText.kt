package fr.outadoc.woolly.htmltext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import fr.outadoc.woolly.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.htmltext.model.FlatNode
import fr.outadoc.woolly.htmltext.model.FlatParagraph
import fr.outadoc.woolly.htmltext.model.FlatTextNode

private const val urlTag = "URL"
private val parser = HtmlParser()

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = Color(0xff64B5F6),
    uriHandler: UriHandler = LocalUriHandler.current
) {
    val annotatedString = buildAnnotatedString {
        appendNodes(parser.parse(text), linkColor)
    }

    MaterialClickableText(
        modifier = modifier,
        text = annotatedString,
        style = style,
        onClick = { index ->
            annotatedString
                .getStringAnnotations(urlTag, index, index)
                .firstOrNull()
                ?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

private fun AnnotatedString.Builder.appendNodes(nodes: List<FlatNode>, linkColor: Color) {
    nodes.forEach { node ->
        when (node) {
            is FlatLinkNode -> {
                pushStyle(
                    SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline
                    )
                )
                pushStringAnnotation(
                    tag = urlTag,
                    annotation = node.href
                )
                appendNodes(node.children, linkColor)
                pop()
                pop()
            }

            is FlatParagraph -> {
                withStyle(ParagraphStyle()) {
                    appendNodes(node.children, linkColor)
                }
            }

            is FlatTextNode -> {
                append(node.text)
            }
        }
    }
}
