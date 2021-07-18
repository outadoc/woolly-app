package fr.outadoc.woolly.htmltext

import fr.outadoc.woolly.htmltext.model.FlatLinkNode
import fr.outadoc.woolly.htmltext.model.FlatNode
import fr.outadoc.woolly.htmltext.model.FlatParagraph
import fr.outadoc.woolly.htmltext.model.FlatTextNode
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

actual class HtmlParser {

    actual fun parse(html: String): List<FlatNode> {
        val preprocessedText = preprocess(html)
        val document = Jsoup.parse(preprocessedText).body()
        return flattenElementsToList(document)
    }

    private val twitterHandleRegex = Regex("@([a-z0-9_]{1,15})@twitter.com")

    private fun preprocess(html: String): String {
        return html.replace(twitterHandleRegex, """<a href="https://twitter.com/$1">$0</a>""")
    }

    private fun flattenElementsToList(element: Element): List<FlatNode> {
        return element.childNodes()
            .mapNotNull { node: Node ->
                when (node) {
                    is TextNode -> listOf(FlatTextNode(node.wholeText))
                    is Element -> {
                        if (node.hasClass("invisible")) {
                            listOf(FlatTextNode(text = "…"))
                        } else when (node.tagName()) {
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
                    else -> null
                }
            }.flatten()
    }
}
