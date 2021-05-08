package fr.outadoc.woolly.common.feature.timeline

import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.htmltext.HtmlParser

class StatusAnnotator(private val parser: HtmlParser) {

    fun annotateStatus(status: Status): AnnotatedStatus {
        return AnnotatedStatus(
            status,
            parser.parse(status.content)
        )
    }
}
