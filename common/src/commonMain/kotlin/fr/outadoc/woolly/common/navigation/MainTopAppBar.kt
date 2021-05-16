package fr.outadoc.woolly.common.navigation

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun MainTopAppBar(title: String) {
    TopAppBar(
        title = { Text(title) }
    )
}
