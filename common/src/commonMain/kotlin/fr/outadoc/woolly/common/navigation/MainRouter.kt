package fr.outadoc.woolly.common.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.home.ui.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.notifications.NotificationsScreen
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.ui.PublicTimelineScreen
import fr.outadoc.woolly.common.feature.publictimeline.ui.PublicTimelineTopAppBar
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.search.ui.SearchTopAppBar
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ColorScheme
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import kotlinx.coroutines.launch
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun MainRouter(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    var currentScreen: AppScreen by rememberSaveable {
        mutableStateOf(AppScreen.HomeTimeline)
    }

    val homeListState = rememberLazyListState()
    val publicLocalListState = rememberLazyListState()
    val publicGlobalListState = rememberLazyListState()
    val notificationsListState = rememberLazyListState()
    val searchStatusesListState = rememberLazyListState()
    val searchAccountsListState = rememberLazyListState()
    val searchHashtagsListState = rememberLazyListState()

    var currentSearchScreen: SearchSubScreen by rememberSaveable {
        mutableStateOf(SearchSubScreen.Statuses)
    }

    var currentPublicTimelineScreen: PublicTimelineSubScreen by rememberSaveable {
        mutableStateOf(PublicTimelineSubScreen.Local)
    }

    val scope = rememberCoroutineScope()
    fun onScreenSelected(selectedScreen: AppScreen) {
        if (selectedScreen != currentScreen) currentScreen = selectedScreen
        else when (selectedScreen) {
            AppScreen.HomeTimeline -> homeListState
            AppScreen.PublicTimeline -> when (currentPublicTimelineScreen) {
                PublicTimelineSubScreen.Global -> publicGlobalListState
                PublicTimelineSubScreen.Local -> publicLocalListState
            }
            AppScreen.Notifications -> notificationsListState
            AppScreen.Search -> when (currentSearchScreen) {
                SearchSubScreen.Statuses -> searchStatusesListState
                SearchSubScreen.Accounts -> searchAccountsListState
                SearchSubScreen.Hashtags -> searchHashtagsListState
            }
            AppScreen.Account -> null
        }?.let { listState ->
            scope.launch { listState.animateScrollToItem(0) }
        }
    }

    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            when (currentScreen) {
                AppScreen.PublicTimeline -> PublicTimelineTopAppBar(
                    title = { Text(res.getScreenTitle(AppScreen.PublicTimeline)) },
                    drawerState = drawerState,
                    currentSubScreen = currentPublicTimelineScreen,
                    onCurrentSubScreenChanged = { currentPublicTimelineScreen = it }
                )

                AppScreen.Search -> SearchTopAppBar(
                    drawerState = drawerState,
                    currentSubScreen = currentSearchScreen,
                    onCurrentSubScreenChanged = { currentSearchScreen = it }
                )

                else -> TopAppBarWithMenu(
                    title = { Text(res.getScreenTitle(currentScreen)) },
                    drawerState = drawerState
                )
            }
        },
        bottomBar = {
            MainBottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { screen -> onScreenSelected(screen) }
            )
        },
        drawerContent = { drawerState ->
            MainAppDrawer(
                drawerState = drawerState,
                colorScheme = colorScheme,
                onColorSchemeChanged = onColorSchemeChanged,
                currentScreen = currentScreen,
                onScreenSelected = { screen -> onScreenSelected(screen) }
            )
        }
    ) { insets ->
        Crossfade(targetState = currentScreen) { screen ->
            when (screen) {
                AppScreen.HomeTimeline -> HomeTimelineScreen(
                    insets = insets,
                    listState = homeListState,
                )

                AppScreen.PublicTimeline -> PublicTimelineScreen(
                    insets = insets,
                    currentSubScreen = currentPublicTimelineScreen,
                    localListState = publicLocalListState,
                    globalListState = publicGlobalListState
                )

                AppScreen.Notifications -> NotificationsScreen(
                    insets = insets,
                    listState = notificationsListState
                )

                AppScreen.Search -> SearchScreen(
                    insets = insets,
                    currentSubScreen = currentSearchScreen,
                    statusListState = searchStatusesListState,
                    accountsListState = searchAccountsListState,
                    hashtagsListState = searchHashtagsListState
                )

                AppScreen.Account -> AccountScreen(insets = insets)
            }
        }
    }
}
