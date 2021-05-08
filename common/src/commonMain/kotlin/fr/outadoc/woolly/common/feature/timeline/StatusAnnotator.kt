package fr.outadoc.woolly.common.feature.timeline

import androidx.compose.ui.graphics.Color
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.htmltext.HtmlParser
import fr.outadoc.woolly.htmltext.parseToAnnotatedString

class StatusAnnotator(private val parser: HtmlParser) {

    fun annotateStatus(status: Status, linkColor: Color): AnnotatedStatus {
        return AnnotatedStatus(
            status,
            parser.parseToAnnotatedString(status.content, linkColor)
        )
    }
}

