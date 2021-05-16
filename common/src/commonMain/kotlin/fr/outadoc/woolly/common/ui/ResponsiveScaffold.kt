package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (Disposition) -> Unit,
    bottomBar: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    BoxWithConstraints {
        if (constraints.maxWidth < with(LocalDensity.current) { 720.dp.toPx() }) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { topBar(Disposition.Standard) },
                bottomBar = bottomBar,
                drawerContent = drawerContent,
                content = content
            )
        } else {
            WideScaffold(
                scaffoldState = scaffoldState,
                topBar = { topBar(Disposition.Wide) },
                drawerContent = drawerContent,
                content = content
            )
        }
    }
}

@Composable
fun WideScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Row {
        Surface(
            modifier = Modifier.width(280.dp),
            color = MaterialTheme.colors.background,
            elevation = DrawerDefaults.Elevation
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                drawerContent()
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = topBar,
            content = content
        )
    }
}
