package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun MainOverlayDrawer(
    preferredTheme: PreferredTheme,
    onColorSchemeChanged: (PreferredTheme) -> Unit = {},
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit = {},
    drawerState: DrawerState? = null,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val authenticationStateConsumer by instance<AuthenticationStateConsumer>()
    val accountRepository by instance<AccountRepository>()
    val account by accountRepository.currentAccount.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        val alpha: Float by animateFloatAsState(if (account == null) 0f else 1f)

        Column(modifier = Modifier.alpha(alpha)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WoollyDefaults.AppBarHeight)
            ) {
                account?.let {
                    OverlayAppDrawerHeader(
                        modifier = Modifier.fillMaxSize(),
                        account = it
                    )
                }
            }

            DrawerListItem(
                title = { Text("Log out") },
                icon = { Icon(Icons.Default.Logout, "Log out") },
                onClick = {
                    scope.launch {
                        authenticationStateConsumer.logoutAll()
                    }
                }
            )
        }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            ScreenItem(
                targetScreen = AppScreen.HomeTimeline,
                selected = currentScreen is AppScreen.HomeTimeline,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.PublicTimeline,
                selected = currentScreen is AppScreen.PublicTimeline,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.Notifications,
                selected = currentScreen is AppScreen.Notifications,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.Search,
                selected = currentScreen is AppScreen.Search,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.Favourites,
                selected = currentScreen is AppScreen.Favourites,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.Bookmarks,
                selected = currentScreen is AppScreen.Bookmarks,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )

            ScreenItem(
                targetScreen = AppScreen.MyAccount,
                selected = currentScreen is AppScreen.MyAccount,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )
        }

        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            when (preferredTheme) {
                PreferredTheme.FollowSystem ->
                    DrawerListItem(
                        title = { Text("Switch to light mode") },
                        icon = { Icon(Icons.Default.MotionPhotosAuto, "Follow system") },
                        onClick = { onColorSchemeChanged(PreferredTheme.Light) }
                    )
                PreferredTheme.Light ->
                    DrawerListItem(
                        title = { Text("Switch to dark mode") },
                        icon = { Icon(Icons.Default.LightMode, "Light mode") },
                        onClick = { onColorSchemeChanged(PreferredTheme.Dark) }
                    )
                PreferredTheme.Dark ->
                    DrawerListItem(
                        title = { Text("Follow system theme") },
                        icon = { Icon(Icons.Default.DarkMode, "Dark mode") },
                        onClick = { onColorSchemeChanged(PreferredTheme.FollowSystem) }
                    )
            }
        }
    }
}

@Composable
private fun ScreenItem(
    scope: CoroutineScope,
    selected: Boolean,
    targetScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    drawerState: DrawerState?
) {
    val res by instance<AppScreenResources>()

    DrawerListItem(
        title = { Text(res.getScreenTitle(targetScreen)) },
        icon = {
            Icon(
                imageVector = res.getScreenIcon(targetScreen),
                contentDescription = res.getScreenTitle(targetScreen)
            )
        },
        onClick = {
            scope.launch { drawerState?.close() }
            onScreenSelected(targetScreen)
        },
        selected = selected
    )
}
