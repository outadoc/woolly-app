package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.status.formatShort

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatusAction(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedColor: Color = LocalContentColor.current,
    counter: Long,
    button: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color =
            if (checked) checkedColor
            else LocalContentColor.current.copy(alpha = ContentAlpha.medium)

        CompositionLocalProvider(
            LocalMinimumTouchTargetEnforcement provides false,
            LocalContentColor provides color
        ) {
            button()

            if (counter > 0) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = counter.formatShort(),
                    maxLines = 1,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
