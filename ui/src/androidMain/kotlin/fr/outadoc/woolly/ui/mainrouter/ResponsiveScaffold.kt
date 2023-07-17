package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldState
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
