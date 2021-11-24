package fr.outadoc.woolly.ui.richtext

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.common.richtext.HtmlParser
import fr.outadoc.woolly.common.richtext.model.*

private val htmlParser = HtmlParser()
private const val ClickableTag = "clickable"

@Composable
fun RichText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = LocalTextStyle.current,
    linkColor: Color = MaterialTheme.colorScheme.secondary,
    emojis: List<Emoji> = emptyList(),
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
        emojis = emojis,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
private fun NodeText(
    modifier: Modifier = Modifier,
    textNodes: List<FlatNode>,
    style: TextStyle = LocalTextStyle.current,
    linkColor: Color = MaterialTheme.colorScheme.secondary,
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
        inlineContent = emojis.associate { emoji ->
            val emojiSize = LocalTextStyle.current.fontSize
            emoji.shortCode to InlineTextContent(
                placeholder = Placeholder(
                    width = emojiSize,
                    height = emojiSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
                ),
                children = {
                    InlineEmoji(
                        modifier = Modifier.size(
                            with(LocalDensity.current) {
                                emojiSize.toDp()
                            }
                        ),
                        emoji = emoji
                    )
                }
            )
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