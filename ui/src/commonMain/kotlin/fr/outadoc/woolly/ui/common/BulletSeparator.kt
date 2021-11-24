package fr.outadoc.woolly.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BulletSeparator(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "•"
    )
}
