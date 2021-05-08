package fr.outadoc.woolly.common.feature.timeline

import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.htmltext.model.FlatNode

data class AnnotatedStatus(
    val original: Status,
    val contentNodes: List<FlatNode>
)