package fr.outadoc.woolly.common.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerMenuButton(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch {
                scaffoldState.drawerState.let {
                    if (it.isClosed) it.open() else it.close()
                }
            }
        }
    ) {
        Icon(Icons.Default.Menu, "Open drawer menu")
    }
}
