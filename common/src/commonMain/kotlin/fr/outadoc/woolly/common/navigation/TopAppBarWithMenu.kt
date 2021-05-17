package fr.outadoc.woolly.common.navigation

import androidx.compose.material.AppBarDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import fr.outadoc.woolly.common.ui.DrawerMenuButton

@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    drawerState: DrawerState?,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    when (drawerState) {
        null -> TopAppBar(
            modifier = modifier,
            title = title,
            backgroundColor = backgroundColor,
            elevation = elevation
        )
        else -> TopAppBar(
            modifier = modifier,
            title = title,
            backgroundColor = backgroundColor,
            elevation = elevation,
            navigationIcon = {
                DrawerMenuButton(drawerState)
            }
        )
    }
}
