package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val formatter: DateTimeFormatter =
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

@Composable
fun AbsoluteTime(
    modifier: Modifier = Modifier,
    time: Instant,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
    ) {
    val formatted = formatter.format(time.toJavaInstant())

    Text(
        text = formatted,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = overflow
    )
}
