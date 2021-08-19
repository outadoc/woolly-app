package com.google.accompanist.systemuicontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSystemUiController(): com.google.accompanist.systemuicontroller.SystemUiController {
    return remember { NoOpSystemUiController }
}
