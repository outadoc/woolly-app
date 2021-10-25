package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.layout.PaddingValues

fun PaddingValues.takeTop() = PaddingValues(top = calculateTopPadding())
fun PaddingValues.takeBottom() = PaddingValues(bottom = calculateBottomPadding())
