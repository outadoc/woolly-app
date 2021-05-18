package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.layout.height
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
import fr.outadoc.woolly.common.ui.WoollyDefaults

@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    drawerState: DrawerState?,
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    TopAppBar(
        modifier = modifier.height(WoollyDefaults.AppBarHeight),
        title = title,
        backgroundColor = backgroundColor,
        elevation = elevation,
        navigationIcon = drawerState?.let {
            @Composable {
                DrawerMenuButton(drawerState)
            }
        }
    )
}
