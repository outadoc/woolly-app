package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun MainTopAppBar(title: String, scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Open drawer menu",
                modifier = Modifier.clickable(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.let {
                                if (it.isClosed) it.open() else it.close()
                            }
                        }
                    }
                )
            )
        }
    )
}
