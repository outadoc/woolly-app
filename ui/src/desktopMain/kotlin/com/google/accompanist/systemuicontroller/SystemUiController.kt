package com.google.accompanist.systemuicontroller

import androidx.compose.ui.graphics.Color

/**
 * A no-op implementation, useful as the default value for [LocalSystemUiController].
 */
internal object NoOpSystemUiController :
    com.google.accompanist.systemuicontroller.SystemUiController {
    override fun setStatusBarColor(
        color: Color,
        darkIcons: Boolean,
        transformColorForLightContent: (Color) -> Color
    ) = Unit

    override fun setNavigationBarColor(
        color: Color,
        darkIcons: Boolean,
        navigationBarContrastEnforced: Boolean,
        transformColorForLightContent: (Color) -> Color
    ) = Unit

    override fun isNavigationBarContrastEnforced(): Boolean = false
}
