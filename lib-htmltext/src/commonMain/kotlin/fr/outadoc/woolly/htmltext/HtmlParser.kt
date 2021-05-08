package fr.outadoc.woolly.htmltext

import fr.outadoc.woolly.htmltext.model.FlatNode

expect class HtmlParser() {
    fun parse(html: String): List<FlatNode>
}
