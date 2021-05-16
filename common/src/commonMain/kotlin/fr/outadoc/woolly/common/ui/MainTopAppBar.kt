package fr.outadoc.woolly.common.ui

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun MainTopAppBar(title: String) {
    TopAppBar(
        title = { Text(title) }
    )
}
