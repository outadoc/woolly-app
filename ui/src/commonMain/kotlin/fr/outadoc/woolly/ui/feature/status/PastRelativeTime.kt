package fr.outadoc.woolly.ui.feature.status

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import fr.outadoc.woolly.common.feature.time.TimeRepository
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.kodein.di.compose.instance
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PastRelativeTime(
    modifier: Modifier = Modifier,
    time: Instant,
    style: TextStyle,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    maxLines: Int = 1
) {
    // Periodically refresh timestamps
    val repository by instance<TimeRepository>()
    val currentTime by repository.currentTime.collectAsState(Clock.System.now())

    val duration = currentTime - time

    val stringDuration = when {
        duration.inWholeSeconds < 5 -> stringResource(
            MR.strings.status_postedAgo_now
        )
        duration.inWholeMinutes < 1 -> stringResource(
            MR.strings.status_postedAgo_seconds,
            duration.inWholeSeconds
        )
        duration.inWholeHours < 1 -> stringResource(
            MR.strings.status_postedAgo_minutes,
            duration.inWholeMinutes
        )
        duration.inWholeDays < 1 -> stringResource(
            MR.strings.status_postedAgo_hours,
            duration.inWholeHours
        )
        else -> stringResource(
            MR.strings.status_postedAgo_days,
            duration.inWholeDays
        )
    }

    Text(
        stringDuration,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines
    )
}
