package fr.outadoc.woolly.common.htmltext

import fr.outadoc.woolly.common.htmltext.model.FlatNode

expect class HtmlParser() {
    fun parse(html: String): List<FlatNode>
}
