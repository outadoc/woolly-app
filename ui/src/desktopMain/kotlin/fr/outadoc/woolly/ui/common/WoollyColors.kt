package fr.outadoc.woolly.ui.common

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun woollyLightColors(): ColorScheme = lightColorScheme(
    primary = Color(0xff2196f3),
    primaryContainer = Color(0xff0069c0),
    onPrimary = Color(0xffffffff),
    secondary = Color(0xff9ccc65),
    secondaryContainer = Color(0xff6b9b37),
    onSecondary = Color(0xff000000)
)

@Composable
actual fun woollyDarkColors(): ColorScheme = darkColorScheme(
    primary = Color(0xff2196f3),
    primaryContainer = Color(0xff0069c0),
    onPrimary = Color(0xffffffff),
    secondary = Color(0xff9ccc65),
    secondaryContainer = Color(0xff6b9b37),
    onSecondary = Color(0xff000000)
)
