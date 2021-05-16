package fr.outadoc.woolly.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkMode) darkColors() else lightColors(),
        content = content
    )
}
