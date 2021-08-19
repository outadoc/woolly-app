package fr.outadoc.woolly.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.ui.screen.AppScreen
import fr.outadoc.woolly.ui.screen.AppScreenResources
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainBottomNavigation(currentScreen: AppScreen, onScreenSelected: (AppScreen) -> Unit) {
    BottomNavigation {
        Item(
            selected = currentScreen is AppScreen.HomeTimeline,
            targetScreen = AppScreen.HomeTimeline,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.PublicTimeline,
            targetScreen = AppScreen.PublicTimeline(),
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.Notifications,
            targetScreen = AppScreen.Notifications,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.Search,
            targetScreen = AppScreen.Search(),
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.Account,
            targetScreen = AppScreen.Account,
            onScreenSelected = onScreenSelected
        )
    }
}

@Composable
private fun RowScope.Item(
    selected: Boolean,
    targetScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()

    val itemTitle = res.getScreenTitle(targetScreen)

    BottomNavigationItem(
        selected = selected,
        onClick = { onScreenSelected(targetScreen) },
        icon = {
            Icon(
                imageVector = res.getScreenIcon(targetScreen),
                contentDescription = itemTitle
            )
        }
    )
}
