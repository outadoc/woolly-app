package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
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
import fr.outadoc.woolly.ui.feature.status.ProfilePicture
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainSideNavigation(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val authenticationStateConsumer by instance<AuthenticationStateConsumer>()
    val accountRepository by instance<AccountRepository>()
    val account by accountRepository.currentAccount.collectAsState()

    NavigationRail(
        header = {
            val alpha: Float by animateFloatAsState(if (account == null) 0f else 1f)

            Column(
                modifier = Modifier
                    .alpha(alpha)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val account = account
                if (account != null) {
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
                        account = account
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
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
        }
    ) {
        ScreenItem(
            selected = currentScreen is AppScreen.HomeTimeline,
            targetScreen = AppScreen.HomeTimeline,
            onScreenSelected = onScreenSelected
        )

        ScreenItem(
            selected = currentScreen is AppScreen.PublicTimeline,
            targetScreen = AppScreen.PublicTimeline,
            onScreenSelected = onScreenSelected
        )

        ScreenItem(
            selected = currentScreen is AppScreen.Notifications,
            targetScreen = AppScreen.Notifications,
            onScreenSelected = onScreenSelected
        )

        ScreenItem(
            selected = currentScreen is AppScreen.Search,
            targetScreen = AppScreen.Search,
            onScreenSelected = onScreenSelected
        )

        ScreenItem(
            selected = currentScreen is AppScreen.Favourites,
            targetScreen = AppScreen.Favourites,
            onScreenSelected = onScreenSelected
        )

        ScreenItem(
            selected = currentScreen is AppScreen.Bookmarks,
            targetScreen = AppScreen.Bookmarks,
            onScreenSelected = onScreenSelected
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun ScreenItem(
    selected: Boolean,
    targetScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val res by instance<AppScreenResources>()
    NavigationRailItem(
        icon = {
            Icon(
                imageVector = res.getScreenIcon(targetScreen),
                contentDescription = res.getScreenTitle(targetScreen)
            )
        },
        selected = selected,
        onClick = { onScreenSelected(targetScreen) }
    )
}
