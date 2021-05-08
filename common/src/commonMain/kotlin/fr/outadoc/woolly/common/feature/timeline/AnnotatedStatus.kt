package fr.outadoc.woolly.common.feature.timeline

import androidx.compose.ui.text.AnnotatedString
import fr.outadoc.mastodonk.api.entity.Status

data class AnnotatedStatus(
    val original: Status,
    val annotatedContent: AnnotatedString
)