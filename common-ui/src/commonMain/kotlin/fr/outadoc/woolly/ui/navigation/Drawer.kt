package fr.outadoc.woolly.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.WoollyListItem
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.screen.AppScreen
import fr.outadoc.woolly.ui.screen.AppScreenResources
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun MainAppDrawer(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit = {},
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
                    AppDrawerHeader(
                        modifier = Modifier.fillMaxSize(),
                        account = it
                    )
                }
            }

            WoollyListItem(
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
                targetScreen = AppScreen.PublicTimeline(),
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
                targetScreen = AppScreen.Search(),
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
                targetScreen = AppScreen.Account,
                selected = currentScreen is AppScreen.Account,
                onScreenSelected = onScreenSelected,
                drawerState = drawerState,
                scope = scope
            )
        }

        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            when (colorScheme) {
                ColorScheme.Light -> {
                    WoollyListItem(
                        title = { Text("Switch to dark mode") },
                        icon = { Icon(Icons.Default.LightMode, "Light mode") },
                        onClick = { onColorSchemeChanged(ColorScheme.Dark) }
                    )
                }
                ColorScheme.Dark -> {
                    WoollyListItem(
                        title = { Text("Switch to light mode") },
                        icon = { Icon(Icons.Default.DarkMode, "Dark mode") },
                        onClick = { onColorSchemeChanged(ColorScheme.Light) }
                    )
                }
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

    WoollyListItem(
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

@Composable
fun AppDrawerHeader(
    modifier: Modifier = Modifier,
    account: Account
) {
    Box(modifier = modifier) {
        ProfileHeader(account = account)
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier, account: Account) {
    Box(modifier) {
        KamelImage(
            modifier = Modifier.fillMaxSize(),
            resource = lazyPainterResource(account.headerStaticUrl),
            contentDescription = "Your profile header",
            crossfade = true,
            contentScale = ContentScale.Crop
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(
                    modifier = Modifier.padding(end = 16.dp),
                    account = account
                )

                Column {
                    Text(
                        text = account.displayNameOrAcct,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (account.displayName.isNotBlank()) {
                        Text(
                            text = "@${account.acct}",
                            style = MaterialTheme.typography.subtitle2,
                            color = LocalContentColor.current.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
