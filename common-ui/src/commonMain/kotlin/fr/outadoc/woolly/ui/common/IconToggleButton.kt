package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun IconToggleButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.small),
        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    ) {
        IconButton(
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            onClick = onClick,
            content = content
        )
    }
}
