package fr.outadoc.woolly.ui.feature.share

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun ShareButton(
    modifier: Modifier,
    shareUrl: String?,
    shareTitle: String?,
    shareText: String?,
    icon: @Composable () -> Unit
) {
}
