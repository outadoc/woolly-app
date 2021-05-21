package fr.outadoc.woolly.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
operator fun PaddingValues.plus(padding: PaddingValues) = PaddingValues(
    start = padding.calculateStartPadding(LocalLayoutDirection.current)
            + calculateStartPadding(LocalLayoutDirection.current),
    end = padding.calculateEndPadding(LocalLayoutDirection.current)
            + calculateEndPadding(LocalLayoutDirection.current),
    top = padding.calculateTopPadding() + calculateTopPadding(),
    bottom = padding.calculateBottomPadding() + calculateBottomPadding()
)
