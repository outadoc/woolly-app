package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DrawerState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable

@Composable
actual fun ResponsiveScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit,
    narrowDrawerContent: @Composable ColumnScope.(DrawerState?) -> Unit,
    wideDrawerContent: @Composable ColumnScope.() -> Unit,
    drawerGesturesEnabled: Boolean,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = @Composable { topBar(scaffoldState.drawerState) },
        bottomBar = bottomBar,
        drawerContent = @Composable { narrowDrawerContent(scaffoldState.drawerState) },
        drawerGesturesEnabled = drawerGesturesEnabled,
        floatingActionButton = floatingActionButton,
        content = content
    )
}
