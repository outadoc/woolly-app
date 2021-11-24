package fr.outadoc.woolly.ui.common

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WoollyTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) woollyDarkColors() else woollyLightColors()

    // Wrap Material3 theme inside Material2 theme until all components are available
    androidx.compose.material.MaterialTheme(
        colors = colorScheme.toMaterial2Colors(isDarkMode)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

private fun ColorScheme.toMaterial2Colors(isDarkMode: Boolean) =
    androidx.compose.material.Colors(
        isLight = !isDarkMode,
        primary = primary,
        primaryVariant = primaryContainer,
        onPrimary = onPrimary,
        secondary = secondary,
        secondaryVariant = secondaryContainer,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError
    )
