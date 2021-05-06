package fr.outadoc.compose.htmltext

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import fr.outadoc.compose.htmltext.model.FlatLinkNode
import fr.outadoc.compose.htmltext.model.FlatNode
import fr.outadoc.compose.htmltext.model.FlatParagraph
import fr.outadoc.compose.htmltext.model.FlatTextNode

private const val urlTag = "URL"
private val parser = HtmlParser()

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    linkColor: Color = Color(0xff64B5F6),
    uriHandler: UriHandler
) {
    val nodes = parser.parse(html)
    val annotatedString = buildAnnotatedString {
        annotate(nodes, linkColor)
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = {
            annotatedString
                .getStringAnnotations(urlTag, it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        }
    )
}

private fun AnnotatedString.Builder.annotate(
    nodes: List<FlatNode>,
    linkColor: Color
) {
    nodes.forEach { node ->
        when (node) {
            is FlatLinkNode -> {
                pushStyle(
                    SpanStyle(
                        color = linkColor,
                        textDecoration = TextDecoration.Underline
                    )
                )
                pushStringAnnotation(tag = urlTag, annotation = node.href)
                append(node.text)
                pop()
                pop()
            }

            is FlatParagraph -> {
                pushStyle(ParagraphStyle())
                annotate(node.children, linkColor)
                pop()
            }

            is FlatTextNode -> {
                append(node.text)
            }
        }
    }
}
