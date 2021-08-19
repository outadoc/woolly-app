package com.google.accompanist.systemuicontroller

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.ViewCompat

/**
 * A helper class for setting the navigation and status bar colors for a [View], gracefully
 * degrading behavior based upon API level.
 *
 * Typically you would use [rememberSystemUiController] to remember an instance of this.
 */
internal class AndroidSystemUiController(view: View) :
    com.google.accompanist.systemuicontroller.SystemUiController {
    private val window = view.context.findWindow()
    private val windowInsetsController = ViewCompat.getWindowInsetsController(view)

    override fun setStatusBarColor(
        color: Color,
        darkIcons: Boolean,
        transformColorForLightContent: (Color) -> Color
    ) {
        windowInsetsController?.isAppearanceLightStatusBars = darkIcons

        window?.statusBarColor = when {
            darkIcons && windowInsetsController?.isAppearanceLightStatusBars != true -> {
                // If we're set to use dark icons, but our windowInsetsController call didn't
                // succeed (usually due to API level), we instead transform the color to maintain
                // contrast
                transformColorForLightContent(color)
            }
            else -> color
        }.toArgb()
    }

    override fun setNavigationBarColor(
        color: Color,
        darkIcons: Boolean,
        navigationBarContrastEnforced: Boolean,
        transformColorForLightContent: (Color) -> Color
    ) {
        windowInsetsController?.isAppearanceLightNavigationBars = darkIcons

        window?.navigationBarColor = when {
            darkIcons && windowInsetsController?.isAppearanceLightNavigationBars != true -> {
                // If we're set to use dark icons, but our windowInsetsController call didn't
                // succeed (usually due to API level), we instead transform the color to maintain
                // contrast
                transformColorForLightContent(color)
            }
            else -> color
        }.toArgb()

        if (Build.VERSION.SDK_INT >= 29) {
            window?.isNavigationBarContrastEnforced = navigationBarContrastEnforced
        }
    }

    override fun isNavigationBarContrastEnforced(): Boolean {
        return Build.VERSION.SDK_INT >= 29 && window?.isNavigationBarContrastEnforced == true
    }

    private fun Context.findWindow(): Window? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context.window
            context = context.baseContext
        }
        return null
    }
}
