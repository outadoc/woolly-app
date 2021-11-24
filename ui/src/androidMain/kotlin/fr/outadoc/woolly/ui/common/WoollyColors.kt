package fr.outadoc.woolly.ui.common

import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun woollyLightColors() = dynamicLightColorScheme(LocalContext.current)

@Composable
actual fun woollyDarkColors() = dynamicDarkColorScheme(LocalContext.current)
