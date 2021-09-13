package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.status.formatShort

@Composable
fun StatusAction(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedColor: Color = LocalContentColor.current,
    icon: ImageVector,
    contentDescription: String,
    counter: Long,
    iconSize: Dp = 22.dp,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val color =
        if (checked) checkedColor
        else LocalContentColor.current.copy(alpha = ContentAlpha.medium)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = checked,
            onCheckedChange = onCheckedChange
        ) {
            Icon(
                icon,
                modifier = Modifier.size(iconSize),
                contentDescription = contentDescription,
                tint = color
            )
        }

        if (counter > 0) {
            Text(
                text = counter.formatShort(),
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
