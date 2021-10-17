package com.google.accompanist.systemuicontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

@Composable
actual fun rememberSystemUiController(): SystemUiController {
    val view = LocalView.current
    return remember(view) { AndroidSystemUiController(view) }
}
