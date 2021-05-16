package fr.outadoc.woolly.common.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.outadoc.woolly.common.ui.Disposition
import fr.outadoc.woolly.common.ui.DrawerMenuButton

@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    scaffoldState: ScaffoldState,
    disposition: Disposition
) {
    when (disposition) {
        Disposition.Standard -> TopAppBar(
            modifier = modifier,
            title = title,
            backgroundColor = backgroundColor,
            navigationIcon = {
                DrawerMenuButton(scaffoldState)
            }
        )
        else -> TopAppBar(
            modifier = modifier,
            title = title,
            backgroundColor = backgroundColor
        )
    }
}
