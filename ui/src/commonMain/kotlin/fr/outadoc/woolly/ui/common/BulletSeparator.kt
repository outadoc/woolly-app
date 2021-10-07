package fr.outadoc.woolly.ui.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BulletSeparator(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "•"
    )
}
