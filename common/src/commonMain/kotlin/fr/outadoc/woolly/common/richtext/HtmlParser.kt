package fr.outadoc.woolly.common.richtext

import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.woolly.common.richtext.model.FlatNode

expect class HtmlParser() {
    fun parse(
        html: String,
        emojis: List<Emoji> = emptyList()
    ): List<FlatNode>
}
