package fr.outadoc.woolly.htmltext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import fr.outadoc.woolly.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.htmltext.model.FlatNode
import fr.outadoc.woolly.htmltext.model.FlatParagraph
import fr.outadoc.woolly.htmltext.model.FlatTextNode

const val URL_TAG = "URL"

fun HtmlParser.parseToAnnotatedString(htmlText: String, linkColor: Color): AnnotatedString {
    return buildAnnotatedString {
        appendNodes(parse(htmlText), linkColor)
    }
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
                    tag = URL_TAG,
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
