package fr.outadoc.woolly.ui.feature.status

import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatusActionColor(
    checked: Boolean = false,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMinimumTouchTargetEnforcement provides false,
        LocalContentAlpha provides if (checked) LocalContentAlpha.current else ContentAlpha.medium
    ) {
        content()
    }
}