package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import fr.outadoc.woolly.ui.common.PaddedTopAppBar
import fr.outadoc.woolly.ui.common.WoollyDefaults

@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    drawerState: DrawerState?,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues = PaddingValues()
) {
    PaddedTopAppBar(
        modifier = modifier
            .height(
                WoollyDefaults.AppBarHeight
                        + contentPadding.calculateTopPadding()
                        + contentPadding.calculateBottomPadding()
            ),
        contentPadding = contentPadding,
        title = title,
        backgroundColor = backgroundColor,
        elevation = elevation,
        navigationIcon = {
            drawerState?.let {
                DrawerMenuButton(drawerState)
            }
        }
    )
}
