package com.google.accompanist.systemuicontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSystemUiController(): SystemUiController {
    return remember { NoOpSystemUiController }
}
