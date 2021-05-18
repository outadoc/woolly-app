package fr.outadoc.woolly.common.navigation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ColorScheme
import fr.outadoc.woolly.common.ui.ProfilePicture
import fr.outadoc.woolly.common.ui.displayNameOrAcct
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainAppDrawer(
    drawerState: DrawerState?,
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit,
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    val authenticationStateConsumer by di.instance<AuthenticationStateConsumer>()

    val screens = listOf(
        AppScreen.HomeTimeline,
        AppScreen.GlobalTimeline,
        AppScreen.LocalTimeline,
        AppScreen.Search
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            AppDrawerHeader()

            DrawerItem(
                title = { Text("Log out") },
                icon = { Icon(Icons.Default.Logout, "Log out") },
                onClick = { authenticationStateConsumer.logoutAll() }
            )
        }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .verticalScroll(scrollState)
        ) {
            screens.forEach { screen ->
                DrawerItem(
                    title = { Text(res.getScreenTitle(screen)) },
                    icon = {
                        Icon(
                            imageVector = res.getScreenIcon(screen),
                            contentDescription = res.getScreenTitle(screen)
                        )
                    },
                    onClick = {
                        scope.launch { drawerState?.close() }
                        onScreenSelected(screen)
                    },
                    selected = currentScreen == screen
                )
            }
        }

        Column(modifier = Modifier.padding(vertical = 16.dp)) {
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

@Composable
fun AppDrawerHeader(modifier: Modifier = Modifier) {
    val di = LocalDI.current
    val repo by di.instance<AccountRepository>()
    val account by repo.currentAccount.collectAsState()
    account?.let {
        ProfileHeader(modifier = modifier, account = it)
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier, account: Account) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f)
    ) {
        val headerResource = lazyImageResource(account.headerStaticUrl) {
            dispatcher = Dispatchers.IO
        }

        KamelImage(
            resource = headerResource,
            contentDescription = "Your profile header",
            crossfade = true,
            animationSpec = tween(),
            contentScale = ContentScale.FillWidth
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
