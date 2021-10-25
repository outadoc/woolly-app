package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.ui.common.PaddedBottomNavigation
import fr.outadoc.woolly.ui.screen.AppScreenResources
import org.kodein.di.compose.instance

@Composable
fun MainBottomNavigation(
    modifier: Modifier = Modifier,
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    PaddedBottomNavigation(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        Item(
            selected = currentScreen is AppScreen.HomeTimeline,
            targetScreen = AppScreen.HomeTimeline,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.PublicTimeline,
            targetScreen = AppScreen.PublicTimeline,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.Notifications,
            targetScreen = AppScreen.Notifications,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.Search,
            targetScreen = AppScreen.Search,
            onScreenSelected = onScreenSelected
        )

        Item(
            selected = currentScreen is AppScreen.MyAccount,
            targetScreen = AppScreen.MyAccount,
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
    val res by instance<AppScreenResources>()
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
