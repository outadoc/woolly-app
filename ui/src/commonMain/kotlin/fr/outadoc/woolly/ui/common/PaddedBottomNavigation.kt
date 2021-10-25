package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PaddedBottomNavigation(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        elevation = elevation,
        modifier = modifier
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = contentColor,
            elevation = 0.dp,
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}