package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun RelativeTime(
    modifier: Modifier,
    time: Instant,
    currentTime: Instant,
    style: TextStyle,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    maxLines: Int
) {
    val duration = currentTime - time

    val stringDuration = when {
        duration.inWholeSeconds < 5 -> "Now"
        duration.inWholeMinutes < 1 -> "${duration.inWholeSeconds} s"
        duration.inWholeHours < 1 -> "${duration.inWholeMinutes} m"
        duration.inWholeDays < 1 -> "${duration.inWholeHours} h"
        else -> "${duration.inWholeDays} d"
    }

    Text(
        stringDuration,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines
    )
}
