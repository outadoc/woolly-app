package fr.outadoc.woolly.common.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.Instant
import kotlin.math.truncate
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun RelativeTime(
    modifier: Modifier,
    time: Instant,
    currentTime: Instant,
    style: TextStyle,
    maxLines: Int
) {
    val duration = currentTime - time

    val stringDuration = when {
        duration.toDouble(DurationUnit.SECONDS) < 5.0 -> "Now"
        duration.toDouble(DurationUnit.MINUTES) < 1.0 -> "${truncate(duration.toDouble(DurationUnit.SECONDS)).toInt()} s"
        duration.toDouble(DurationUnit.HOURS) < 1.0 -> "${truncate(duration.toDouble(DurationUnit.MINUTES)).toInt()} m"
        duration.toDouble(DurationUnit.DAYS) < 1.0 -> "${truncate(duration.toDouble(DurationUnit.DAYS)).toInt()} h"
        else -> "${truncate(duration.toDouble(DurationUnit.DAYS)).toInt()} d"
    }

    Text(
        stringDuration,
        modifier = modifier,
        style = style,
        maxLines = maxLines
    )
}
