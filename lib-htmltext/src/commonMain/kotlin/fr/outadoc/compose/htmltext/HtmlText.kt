package fr.outadoc.compose.htmltext

import androidx.compose.foundation.text.ClickableText
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
import fr.outadoc.compose.htmltext.model.FlatLinkNode
import fr.outadoc.compose.htmltext.model.FlatNode
import fr.outadoc.compose.htmltext.model.FlatParagraph
import fr.outadoc.compose.htmltext.model.FlatTextNode

private const val urlTag = "URL"
private val parser = HtmlParser()

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = Color(0xff64B5F6),
    uriHandler: UriHandler = LocalUriHandler.current,
    uriTitleProvider: (String) -> String? = { it }
) {
    val annotatedString = buildAnnotatedString {
        annotate(parser.parse(text), linkColor, uriTitleProvider)
    }

    ClickableText(
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

private fun AnnotatedString.Builder.annotate(
    nodes: List<FlatNode>,
    linkColor: Color,
    uriTitleProvider: (String) -> String?
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
                pushStringAnnotation(
                    tag = urlTag,
                    annotation = node.href
                )
                append(uriTitleProvider(node.href) ?: node.text)
                pop()
                pop()
            }

            is FlatParagraph -> {
                withStyle(ParagraphStyle()) {
                    annotate(node.children, linkColor, uriTitleProvider)
                }
            }

            is FlatTextNode -> {
                append(node.text)
            }
        }
    }
}
