package fr.outadoc.woolly.common.htmltext

import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.common.htmltext.model.FlatNode

expect class HtmlParser() {
    fun parse(html: String, emojis: List<Emoji>): List<FlatNode>
}
