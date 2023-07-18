package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun DrawerMenuButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            Icons.Default.Menu,
            contentDescription = stringResource(MR.strings.navigation_openDrawer_cd)
        )
    }
}
