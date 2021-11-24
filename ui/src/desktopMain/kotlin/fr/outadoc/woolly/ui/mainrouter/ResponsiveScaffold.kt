package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DrawerState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

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
    Row {
        Column {
            wideDrawerContent()
        }

        Surface(
            modifier = Modifier.zIndex(0.1f),
            color = MaterialTheme.colorScheme.background,
            elevation = 4.dp
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = @Composable { topBar(null) },
                drawerContent = wideDrawerContent,
                drawerGesturesEnabled = drawerGesturesEnabled,
                floatingActionButton = floatingActionButton,
                content = content
            )
        }
    }
}
