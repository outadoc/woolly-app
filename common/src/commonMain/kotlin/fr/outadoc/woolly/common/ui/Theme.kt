package fr.outadoc.woolly.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkMode) darkColors() else lightColors(),
    ) {
        val systemUiController = rememberSystemUiController()
        val systemBarsColor =
            if (isDarkMode) MaterialTheme.colors.surface
            else MaterialTheme.colors.primaryVariant

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = systemBarsColor,
                darkIcons = false
            )
        }

        content()
    }
}
