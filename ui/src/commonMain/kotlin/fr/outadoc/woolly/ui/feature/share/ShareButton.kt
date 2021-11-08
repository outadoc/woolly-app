package fr.outadoc.woolly.ui.feature.share

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ShareButton(
    modifier: Modifier = Modifier,
    shareUrl: String? = null,
    shareTitle: String? = null,
    shareText: String? = null,
    icon: @Composable () -> Unit
)
