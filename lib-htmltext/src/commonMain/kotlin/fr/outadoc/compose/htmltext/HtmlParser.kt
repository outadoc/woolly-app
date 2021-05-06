package fr.outadoc.compose.htmltext

import fr.outadoc.compose.htmltext.model.FlatNode

expect class HtmlParser() {
    fun parse(html: String): List<FlatNode>
}
