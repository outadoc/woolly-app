package fr.outadoc.woolly.htmltext

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import fr.outadoc.woolly.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.htmltext.model.FlatNode
import fr.outadoc.woolly.htmltext.model.FlatParagraph
import fr.outadoc.woolly.htmltext.model.FlatTextNode

private val htmlParser = HtmlParser()
private const val ClickableTag = "clickable"

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = MaterialTheme.colors.secondary,
    uriHandler: UriHandler = LocalUriHandler.current
) {
    val textNodes = remember(html) {
        htmlParser.parse(html)
    }

    NodeText(modifier, textNodes, style, linkColor, uriHandler)
}

@Composable
fun NodeText(
    modifier: Modifier = Modifier,
    textNodes: List<FlatNode>,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = MaterialTheme.colors.secondary,
    uriHandler: UriHandler = LocalUriHandler.current
) {
    val annotatedString = buildAnnotatedString {
        appendNodes(textNodes, linkColor)
    }

    MaterialClickableText(
        modifier = modifier,
        text = annotatedString,
        style = style,
        isClickableIndex = { index ->
            annotatedString
                .getStringAnnotations(ClickableTag, index, index)
                .any()
        },
        onClick = { index ->
            annotatedString
                .getStringAnnotations(ClickableTag, index, index)
                .firstOrNull()
                ?.item
                ?.let { url ->
                    uriHandler.openUri(url)
                }
        }
    )
}

private fun AnnotatedString.Builder.appendNodes(nodes: List<FlatNode>, linkColor: Color) {
    nodes.forEachIndexed { index, node ->
        when (node) {
            is FlatLinkNode -> {
                withStyle(SpanStyle(color = linkColor)) {
                    withAnnotation(ClickableTag, node.href) {
                        appendNodes(node.children, linkColor)
                    }
                }
            }

            is FlatParagraph -> {
                appendNodes(node.children, linkColor)

                if (index < nodes.size - 1) {
                    append("\n\n")
                }
            }

            is FlatTextNode -> {
                append(node.text)
            }
        }
    }
}

private inline fun AnnotatedString.Builder.withAnnotation(
    tag: String,
    annotation: String,
    crossinline block: AnnotatedString.Builder.() -> Unit
) {
    pushStringAnnotation(tag, annotation)
    this.block()
    pop()
}