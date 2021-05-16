package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainAppDrawer(
    toggleDarkMode: () -> Unit,
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res: AppScreenResources by di.instance()

    val screens = listOf(
        AppScreen.LocalTimeline,
        AppScreen.GlobalTimeline,
        AppScreen.Search
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            screens.forEach { screen ->
                DrawerItem(
                    title = { Text(res.getScreenTitle(screen)) },
                    icon = {
                        Icon(
                            imageVector = res.getScreenIcon(screen),
                            contentDescription = res.getScreenTitle(screen)
                        )
                    },
                    onClick = { onScreenSelected(screen) },
                    selected = currentScreen == screen
                )
            }
        }

        Column {
            DrawerItem(
                title = { Text("Toggle dark mode") },
                icon = { Icon(Icons.Default.ModeNight, "Toggle dark mode") },
                onClick = toggleDarkMode
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    Surface(
        color = if (selected) {
            MaterialTheme.colors.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        },
        contentColor = if (selected) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface
        }
    ) {
        ListItem(
            modifier = Modifier.clickable(
                role = Role.Button,
                onClick = onClick
            ),
            icon = icon
        ) {
            title()
        }
    }
}
