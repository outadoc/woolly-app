package fr.outadoc.mastodonk.common

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    isDarkModeEnabled: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkModeEnabled) darkColors() else lightColors(),
        content = content
    )
}
