package fr.outadoc.woolly.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WoollyTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkMode) woollyDarkColors() else woollyLightColors(),
        content = content,
    )
}
