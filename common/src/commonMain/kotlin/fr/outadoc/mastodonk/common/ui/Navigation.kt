package fr.outadoc.mastodonk.common.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable

@Composable
fun MainTopAppBar(title: String, toggleDarkMode: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { toggleDarkMode() }) {
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Toggle dark theme"
                )
            }
        }
    )
}
