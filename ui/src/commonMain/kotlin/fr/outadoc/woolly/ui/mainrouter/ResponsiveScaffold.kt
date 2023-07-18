package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable

@Composable
expect fun ResponsiveScaffold(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    narrowDrawerContent: @Composable ColumnScope.() -> Unit,
    wideDrawerContent: @Composable ColumnScope.() -> Unit,
    drawerGesturesEnabled: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
)
