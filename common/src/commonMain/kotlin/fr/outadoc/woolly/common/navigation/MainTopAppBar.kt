package fr.outadoc.woolly.common.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.ui.Disposition
import fr.outadoc.woolly.common.ui.DrawerMenuButton

@Composable
fun MainTopAppBar(
    title: String,
    scaffoldState: ScaffoldState,
    disposition: Disposition
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (disposition == Disposition.Standard) {
                DrawerMenuButton(scaffoldState)
            }
        }
    )
}
