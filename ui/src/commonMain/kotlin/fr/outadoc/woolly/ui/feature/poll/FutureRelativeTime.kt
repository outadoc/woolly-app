package fr.outadoc.woolly.ui.feature.poll

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import fr.outadoc.woolly.common.feature.time.TimeRepository
import fr.outadoc.woolly.ui.strings.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.kodein.di.compose.instance
import kotlin.time.ExperimentalTime
import fr.outadoc.woolly.ui.MR

@OptIn(ExperimentalTime::class)
@Composable
fun FutureRelativeTime(
    modifier: Modifier = Modifier,
    time: Instant,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    maxLines: Int = 1,
    expiredLabel: String
) {
    // Periodically refresh timestamps
    val repository by instance<TimeRepository>()
    val currentTime by repository.currentTime.collectAsState(Clock.System.now())

    val duration = time - currentTime

    val stringDuration = when {
        duration.inWholeSeconds < 5 -> expiredLabel
        duration.inWholeMinutes < 1 -> stringResource(MR.strings.poll_expirationTime_seconds, duration.inWholeSeconds)
        duration.inWholeHours < 1 -> stringResource(MR.strings.poll_expirationTime_minutes, duration.inWholeMinutes)
        duration.inWholeDays < 1 -> stringResource(MR.strings.poll_expirationTime_hours, duration.inWholeHours)
        else -> stringResource(MR.strings.poll_expirationTime_days, duration.inWholeDays)
    }

    Text(
        stringDuration,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines
    )
}
