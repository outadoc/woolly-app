package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerMenuButton(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch {
                drawerState.let {
                    if (it.isClosed) it.open() else it.close()
                }
            }
        }
    ) {
        Icon(Icons.Default.Menu, "Open drawer menu")
    }
}
