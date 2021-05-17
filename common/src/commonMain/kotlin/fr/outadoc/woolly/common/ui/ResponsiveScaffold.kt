package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit,
    drawerContent: @Composable ColumnScope.(DrawerState?) -> Unit,
    breakpointWidthDp: Dp = 720.dp,
    content: @Composable (PaddingValues) -> Unit
) {
    BoxWithConstraints {
        val breakpointWidthPx = with(LocalDensity.current) { breakpointWidthDp.toPx() }
        val disposition = if (constraints.maxWidth >= breakpointWidthPx) {
            Disposition.Wide
        } else {
            Disposition.Standard
        }

        Row {
            if (disposition == Disposition.Wide) {
                Surface(
                    modifier = Modifier.width(270.dp),
                    color = MaterialTheme.colors.background,
                    elevation = DrawerDefaults.Elevation
                ) {
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        drawerContent(null)
                    }
                }
            }

            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    topBar(
                        if (disposition == Disposition.Standard) {
                            scaffoldState.drawerState
                        } else null
                    )
                },
                bottomBar = {
                    if (disposition == Disposition.Standard) {
                        bottomBar()
                    }
                },
                drawerContent = if (disposition == Disposition.Standard) {
                    @Composable { drawerContent(scaffoldState.drawerState) }
                } else null,
                content = content
            )
        }
    }
}
