package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.outadoc.woolly.ui.common.PaddedTopAppBar
import fr.outadoc.woolly.ui.common.WoollyDefaults

@Composable
fun TopAppBarWithMenu(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(),
    onClickNavigationIcon: () -> Unit
) {
    PaddedTopAppBar(
        modifier = modifier
            .height(
                WoollyDefaults.AppBarHeight
                        + contentPadding.calculateTopPadding()
                        + contentPadding.calculateBottomPadding()
            ),
        contentPadding = contentPadding,
        title = title,
        backgroundColor = backgroundColor,
        navigationIcon = {
            DrawerMenuButton(
                onClick = onClickNavigationIcon
            )
        }
    )
}
