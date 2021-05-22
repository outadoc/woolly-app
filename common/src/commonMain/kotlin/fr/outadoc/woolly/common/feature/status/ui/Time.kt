package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.Instant
import kotlin.math.truncate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun RelativeTime(
    modifier: Modifier,
    time: Instant,
    currentTime: Instant,
    style: TextStyle,
    color: Color = LocalContentColor.current,
    maxLines: Int
) {
    val duration = currentTime - time

    val stringDuration = when {
        duration.inSeconds < 5.0 -> "Now"
        duration.inMinutes < 1.0 -> "${truncate(duration.inSeconds).toInt()} s"
        duration.inHours < 1.0 -> "${truncate(duration.inMinutes).toInt()} m"
        duration.inDays < 1.0 -> "${truncate(duration.inHours).toInt()} h"
        else -> "${truncate(duration.inDays).toInt()} d"
    }

    Text(
        stringDuration,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines
    )
}
