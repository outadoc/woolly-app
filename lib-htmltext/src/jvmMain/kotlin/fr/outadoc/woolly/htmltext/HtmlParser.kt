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
        val document = Jsoup.parse(html)
        return flattenElementsToList(document.body())
    }

    private fun flattenElementsToList(element: Element): List<FlatNode> {
        return element.childNodes()
            .mapNotNull { node: Node ->
                when (node) {
                    is TextNode -> listOf(FlatTextNode(node.text()))
                    is Element -> {
                        if (node.hasClass("invisible")) emptyList()
                        else when (node.tagName()) {
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
