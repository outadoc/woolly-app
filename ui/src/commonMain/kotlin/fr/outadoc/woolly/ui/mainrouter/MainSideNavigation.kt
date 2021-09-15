package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun MainSideNavigation(
    preferredTheme: PreferredTheme,
    onColorSchemeChanged: (PreferredTheme) -> Unit = {},
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val authenticationStateConsumer by instance<AuthenticationStateConsumer>()
    val accountRepository by instance<AccountRepository>()
    val account by accountRepository.currentAccount.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val alpha: Float by animateFloatAsState(if (account == null) 0f else 1f)

        Column(
            modifier = Modifier
                .alpha(alpha)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            account?.let {
                ProfilePicture(
                    modifier = Modifier
                        .padding(4.dp)
                        .alpha(
                            if (currentScreen is AppScreen.MyAccount) LocalContentAlpha.current
                            else ContentAlpha.medium
                        ),
                    onClick = {
                        onScreenSelected(AppScreen.MyAccount)
                    },
                    size = 32.dp,
                    account = it
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        authenticationStateConsumer.logoutAll()
                    }
                }
            ) {
                Icon(Icons.Default.Logout, "Log out")
            }
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
                onScreenSelected = onScreenSelected
            )

            ScreenItem(
                targetScreen = AppScreen.PublicTimeline,
                selected = currentScreen is AppScreen.PublicTimeline,
                onScreenSelected = onScreenSelected
            )

            ScreenItem(
                targetScreen = AppScreen.Notifications,
                selected = currentScreen is AppScreen.Notifications,
                onScreenSelected = onScreenSelected
            )

            ScreenItem(
                targetScreen = AppScreen.Search,
                selected = currentScreen is AppScreen.Search,
                onScreenSelected = onScreenSelected
            )

            ScreenItem(
                targetScreen = AppScreen.Favourites,
                selected = currentScreen is AppScreen.Favourites,
                onScreenSelected = onScreenSelected
            )

            ScreenItem(
                targetScreen = AppScreen.Bookmarks,
                selected = currentScreen is AppScreen.Bookmarks,
                onScreenSelected = onScreenSelected
            )
        }

        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            when (preferredTheme) {
                PreferredTheme.FollowSystem ->
                    IconButton(onClick = { onColorSchemeChanged(PreferredTheme.Light) }) {
                        Icon(Icons.Default.DarkMode, "Dark mode")
                    }

                PreferredTheme.Light ->
                    IconButton(onClick = { onColorSchemeChanged(PreferredTheme.Dark) }) {
                        Icon(Icons.Default.LightMode, "Light mode")
                    }

                PreferredTheme.Dark ->
                    IconButton(onClick = { onColorSchemeChanged(PreferredTheme.FollowSystem) }) {
                        Icon(Icons.Default.MotionPhotosAuto, "Follow system")
                    }
            }
        }
    }
}

@Composable
private fun ScreenItem(
    selected: Boolean,
    targetScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val res by instance<AppScreenResources>()

    IconButton(
        modifier = Modifier.padding(4.dp),
        onClick = { onScreenSelected(targetScreen) }
    ) {
        Icon(
            imageVector = res.getScreenIcon(targetScreen),
            contentDescription = res.getScreenTitle(targetScreen),
            tint = if (selected) selectedContentColor else unselectedContentColor
        )
    }
}
