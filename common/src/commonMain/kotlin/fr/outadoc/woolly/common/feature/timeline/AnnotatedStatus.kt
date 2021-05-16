package fr.outadoc.woolly.common.feature.timeline

import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.htmltext.model.FlatNode

data class AnnotatedStatus(
    val original: Status,
    val boostedBy: Account?,
    val contentNodes: List<FlatNode>
)
