package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.zIndex

private enum class Disposition {
    Standard, Wide
}

@Composable
fun ResponsiveScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit,
    narrowDrawerContent: @Composable ColumnScope.(DrawerState?) -> Unit,
    wideDrawerContent: @Composable ColumnScope.() -> Unit,
    breakpointWidthDp: Dp = 600.dp,
    content: @Composable (PaddingValues) -> Unit
) {
    BoxWithConstraints {
        val breakpointWidthPx = with(LocalDensity.current) { breakpointWidthDp.toPx() }
        val disposition =
            if (constraints.maxWidth >= breakpointWidthPx) Disposition.Wide
            else Disposition.Standard

        when (disposition) {
            Disposition.Wide -> {
                Row {
                    Surface(
                        modifier = Modifier
                            .width(64.dp)
                            .zIndex(0.0f),
                        color = MaterialTheme.colors.background
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            wideDrawerContent()
                        }
                    }

                    Surface(
                        modifier = Modifier.zIndex(0.1f),
                        color = MaterialTheme.colors.background,
                        elevation = 4.dp
                    ) {
                        InnerScaffold(
                            scaffoldState = scaffoldState,
                            topBar = topBar,
                            bottomBar = bottomBar,
                            drawerContent = {},
                            content = content,
                            disposition = disposition
                        )
                    }
                }
            }
            Disposition.Standard -> {
                InnerScaffold(
                    scaffoldState = scaffoldState,
                    topBar = topBar,
                    bottomBar = bottomBar,
                    drawerContent = narrowDrawerContent,
                    content = content,
                    disposition = disposition
                )
            }
        }
    }
}

@Composable
private fun InnerScaffold(
    scaffoldState: ScaffoldState,
    topBar: @Composable (DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit,
    drawerContent: @Composable (ColumnScope.(DrawerState?) -> Unit),
    content: @Composable (PaddingValues) -> Unit,
    disposition: Disposition
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            topBar(
                if (disposition == Disposition.Standard) scaffoldState.drawerState
                else null
            )
        },
        bottomBar = {
            if (disposition == Disposition.Standard) bottomBar()
        },
        drawerContent = if (disposition == Disposition.Standard) {
            @Composable { drawerContent(scaffoldState.drawerState) }
        } else null,
        content = content
    )
}
