package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DrawerState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable

@Composable
expect fun ResponsiveScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit,
    narrowDrawerContent: @Composable ColumnScope.(DrawerState?) -> Unit,
    wideDrawerContent: @Composable ColumnScope.() -> Unit,
    drawerGesturesEnabled: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
)
