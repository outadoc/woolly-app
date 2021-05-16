package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoSubscriber
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ColorScheme
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainAppDrawer(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit,
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    val authInfoSubscriber by di.instance<AuthInfoSubscriber>()

    val screens = listOf(
        AppScreen.HomeTimeline,
        AppScreen.GlobalTimeline,
        AppScreen.LocalTimeline,
        AppScreen.Search
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
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
                title = { Text("Log out") },
                icon = { Icon(Icons.Default.Logout, "Log out") },
                onClick = { authInfoSubscriber.publish(null) }
            )

            when (colorScheme) {
                ColorScheme.Light -> {
                    DrawerItem(
                        title = { Text("Switch to dark mode") },
                        icon = { Icon(Icons.Default.LightMode, "Light mode") },
                        onClick = { onColorSchemeChanged(ColorScheme.Dark) }
                    )
                }
                ColorScheme.Dark -> {
                    DrawerItem(
                        title = { Text("Switch to light mode") },
                        icon = { Icon(Icons.Default.DarkMode, "Dark mode") },
                        onClick = { onColorSchemeChanged(ColorScheme.Light) }
                    )
                }
            }
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
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(MaterialTheme.shapes.small),
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
