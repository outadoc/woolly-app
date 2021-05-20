package com.google.accompanist.systemuicontroller

import androidx.compose.runtime.Composable

/**
 * Remembers a [SystemUiController] for the current device.
 */
@Composable
expect fun rememberSystemUiController(): SystemUiController
