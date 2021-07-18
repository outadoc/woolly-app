package fr.outadoc.woolly.common.htmltext

import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.common.htmltext.model.FlatEmojiNode
import fr.outadoc.woolly.common.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.common.htmltext.model.FlatNode
import fr.outadoc.woolly.common.htmltext.model.FlatParagraph
import fr.outadoc.woolly.common.htmltext.model.FlatTextNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

actual class HtmlParser {

    actual fun parse(html: String, emojis: List<Emoji>): List<FlatNode> {
        val preprocessedText = preprocess(html, emojis)
        val document = Jsoup.parse(preprocessedText).body()
        return flattenElementsToList(document)
    }

    private val twitterHandleRegex = Regex("@([a-z0-9_]{1,15})@twitter.com")

    private fun preprocess(html: String, emojis: List<Emoji>): String {
        return emojis.fold(html) { acc: String, emoji: Emoji ->
            // Replace emoji with a custom <span>
            val placeholder = ":${emoji.shortCode}:"
            acc.replace(
                placeholder,
                """<span class="x-emoji" href="${emoji.shortCode}">$placeholder</span>"""
            )
        }.replace(
            // Replace twitter usernames with Twitter link
            twitterHandleRegex,
            """<a href="https://twitter.com/$1">$0</a>"""
        )
    }

    private fun flattenElementsToList(element: Element): List<FlatNode> {
        return element.childNodes()
            .mapNotNull { node: Node ->
                when (node) {
                    is TextNode -> listOf(FlatTextNode(node.wholeText))
                    is Element -> {
                        when {
                            node.hasClass("invisible") -> listOf(
                                FlatTextNode(text = "…")
                            )
                            node.hasClass("x-emoji") -> listOf(
                                FlatEmojiNode(shortCode = node.attr("href"))
                            )
                            else -> when (node.tagName()) {
                                "a" -> listOf(
                                    FlatLinkNode(
                                        href = node.attr("href"),
                                        children = flattenElementsToList(node)
                                    )
                                )
                                "p" -> listOf(FlatParagraph(children = flattenElementsToList(node)))
                                "br" -> listOf(FlatTextNode(text = "\n"))
                                else -> flattenElementsToList(node)
                            }
                        }
                    }
                    else -> null
                }
            }.flatten()
    }
}
