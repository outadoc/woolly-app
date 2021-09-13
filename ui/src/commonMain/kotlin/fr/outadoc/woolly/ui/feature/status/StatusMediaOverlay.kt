package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.ui.common.WoollyTheme

@Composable
fun StatusMediaOverlay(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    contentDescription: String?,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()

        if (icon != null) {
            Surface(
                shape = WoollyTheme.AvatarShape,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        }
    }
}
