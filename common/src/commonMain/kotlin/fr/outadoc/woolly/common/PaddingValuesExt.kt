package fr.outadoc.woolly.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp

operator fun PaddingValues.plus(dp: Dp) = PaddingValues(
    start = dp,
    end = dp,
    top = dp + calculateTopPadding(),
    bottom = dp + calculateBottomPadding()
)
