package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.takeBottom
import fr.outadoc.woolly.ui.common.takeTop
import fr.outadoc.woolly.ui.screen.getIcon
import fr.outadoc.woolly.ui.screen.getTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun MainOverlayDrawer(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit = {},
    drawerState: DrawerState? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val authenticationStateConsumer by instance<AuthenticationStateConsumer>()
    val accountRepository by instance<AccountRepository>()
    val account by accountRepository.currentAccount.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val alpha: Float by animateFloatAsState(if (account == null) 0f else 1f)

        Column(modifier = Modifier.alpha(alpha)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WoollyDefaults.AppBarHeight + contentPadding.calculateTopPadding())
            ) {
                account?.let {
                    OverlayAppDrawerHeader(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding.takeTop(),
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

        Box(modifier = Modifier.weight(1f, fill = true)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 16.dp)
                    .padding(contentPadding.takeBottom())
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
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
    DrawerListItem(
        title = { Text(targetScreen.getTitle()) },
        icon = {
            Icon(
                imageVector = targetScreen.getIcon(),
                contentDescription = targetScreen.getTitle()
            )
        },
        onClick = {
            scope.launch { drawerState?.close() }
            onScreenSelected(targetScreen)
        },
        selected = selected
    )
}
