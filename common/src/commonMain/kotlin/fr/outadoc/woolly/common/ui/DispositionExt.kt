package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Stable
val FillFirstThenWrap = object : Arrangement.Horizontal {

    override val spacing = 0.dp

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        layoutDirection: LayoutDirection,
        outPositions: IntArray
    ) = if (layoutDirection == LayoutDirection.Ltr) {
        place(totalSize, sizes.reversedArray(), outPositions)
    } else {
        place(totalSize, sizes, outPositions)
    }

    private fun place(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        var remaining = totalSize

        // sizes is reversed, leftmost composable is last in the list (for LTR)
        sizes.forEachIndexed { i, size ->
            val realIndex = sizes.size - 1 - i
            if (i == sizes.size - 1) {
                // Leftmost composable
                outPositions[realIndex] = 0
            } else {
                outPositions[realIndex] = remaining - size
                remaining -= size
            }
        }
    }

    override fun toString() = "Arrangement#FillFirstThenWrap"
}
