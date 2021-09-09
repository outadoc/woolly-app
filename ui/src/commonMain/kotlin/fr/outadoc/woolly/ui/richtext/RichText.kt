package fr.outadoc.woolly.ui.richtext

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.common.richtext.HtmlParser
import fr.outadoc.woolly.common.richtext.model.FlatEmojiNode
import fr.outadoc.woolly.common.richtext.model.FlatLinkNode
import fr.outadoc.woolly.common.richtext.model.FlatNode
import fr.outadoc.woolly.common.richtext.model.FlatParagraph
import fr.outadoc.woolly.common.richtext.model.FlatTextNode
import fr.outadoc.woolly.ui.common.InlineEmoji

private val htmlParser = HtmlParser()
private const val ClickableTag = "clickable"

@Composable
fun RichText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = MaterialTheme.colors.secondary,
    uriHandler: UriHandler = LocalUriHandler.current,
    emojis: List<Emoji>,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val textNodes = remember(text) {
        htmlParser.parse(text, emojis)
    }

    NodeText(
        modifier = modifier,
        textNodes = textNodes,
        style = style,
        linkColor = linkColor,
        uriHandler = uriHandler,
        emojis = emojis,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
private fun NodeText(
    modifier: Modifier = Modifier,
    textNodes: List<FlatNode>,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = MaterialTheme.colors.secondary,
    uriHandler: UriHandler = LocalUriHandler.current,
    emojis: List<Emoji>,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val annotatedString = buildAnnotatedString {
        appendNodes(textNodes, linkColor)
    }

    MaterialClickableText(
        text = annotatedString,
        modifier = modifier,
        style = style,
        isClickableIndex = { index ->
            annotatedString
                .getStringAnnotations(ClickableTag, index, index)
                .any()
        },
        maxLines = maxLines,
        overflow = overflow,
        onClick = { index ->
            annotatedString
                .getStringAnnotations(ClickableTag, index, index)
                .firstOrNull()
                ?.item
                ?.let { url ->
                    uriHandler.openUri(url)
                }
        },
        inlineContent = emojis.map { emoji ->
            emoji.shortCode to InlineTextContent(
                Placeholder(
                    width = 1.3.em,
                    height = 1.3.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
                )
            ) {
                InlineEmoji(emoji)
            }
        }.toMap()
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

            is FlatEmojiNode -> {
                appendInlineContent(
                    id = node.shortCode,
                    alternateText = ":${node.shortCode}:"
                )
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