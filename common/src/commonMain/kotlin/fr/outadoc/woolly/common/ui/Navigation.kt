package fr.outadoc.woolly.common.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainTopAppBar(title: String, toggleDarkMode: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = { toggleDarkMode() }) {
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Toggle dark theme"
                )
            }
        }
    )
}

@Composable
fun MainBottomNavigation(currentScreen: AppScreen, onScreenSelected: (AppScreen) -> Unit) {
    BottomNavigation {
        Item(
            currentScreen = currentScreen,
            screen = AppScreen.GlobalTimeline,
            onScreenSelected = onScreenSelected
        )

        Item(
            currentScreen = currentScreen,
            screen = AppScreen.LocalTimeline,
            onScreenSelected = onScreenSelected
        )
    }
}

@Composable
private fun RowScope.Item(
    currentScreen: AppScreen,
    screen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res: AppScreenResources by di.instance()

    val itemTitle = res.getScreenTitle(screen)

    BottomNavigationItem(
        label = {
            Text(text = itemTitle)
        },
        selected = currentScreen == screen,
        onClick = { onScreenSelected(screen) },
        alwaysShowLabel = false,
        icon = {
            Icon(
                imageVector = res.getScreenIcon(screen),
                contentDescription = itemTitle
            )
        }
    )
}
