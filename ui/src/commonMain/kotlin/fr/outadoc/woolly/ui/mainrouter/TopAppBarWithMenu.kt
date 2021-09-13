package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import fr.outadoc.woolly.ui.common.WoollyDefaults

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
