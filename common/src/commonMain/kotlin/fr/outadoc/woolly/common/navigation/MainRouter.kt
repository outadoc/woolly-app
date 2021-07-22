package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.replaceCurrent
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.bookmarks.ui.BookmarksScreen
import fr.outadoc.woolly.common.feature.favourites.ui.FavouritesScreen
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

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainRouter(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    val homeListState = rememberLazyListState()
    val publicLocalListState = rememberLazyListState()
    val publicGlobalListState = rememberLazyListState()
    val notificationsListState = rememberLazyListState()
    val searchStatusesListState = rememberLazyListState()
    val searchAccountsListState = rememberLazyListState()
    val searchHashtagsListState = rememberLazyListState()
    val bookmarksListState = rememberLazyListState()
    val favouritesListState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    fun AppScreen.scrollToTop() {
        when (this) {
            AppScreen.HomeTimeline -> homeListState
            is AppScreen.PublicTimeline -> when (subScreen) {
                PublicTimelineSubScreen.Global -> publicGlobalListState
                PublicTimelineSubScreen.Local -> publicLocalListState
            }
            AppScreen.Notifications -> notificationsListState
            is AppScreen.Search -> when (subScreen) {
                SearchSubScreen.Statuses -> searchStatusesListState
                SearchSubScreen.Accounts -> searchAccountsListState
                SearchSubScreen.Hashtags -> searchHashtagsListState
            }
            AppScreen.Account -> null
            AppScreen.Bookmarks -> bookmarksListState
            AppScreen.Favourites -> favouritesListState
        }?.let { listState ->
            scope.launch {
                try {
                    listState.animateScrollToItem(0)
                } catch (e: NoSuchElementException) {
                    // List was empty
                }
            }
        }
    }

    val router = rememberRouter<AppScreen>(
        initialConfiguration = { AppScreen.HomeTimeline },
        handleBackButton = true
    )

    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            Children(routerState = router.state) { screen ->
                when (val currentScreen = screen.configuration) {
                    is AppScreen.PublicTimeline -> PublicTimelineTopAppBar(
                        title = { Text(res.getScreenTitle(currentScreen)) },
                        drawerState = drawerState,
                        currentSubScreen = currentScreen.subScreen,
                        onCurrentSubScreenChanged = { subScreen ->
                            when (currentScreen.subScreen) {
                                subScreen -> currentScreen.scrollToTop()
                                else -> router.replaceCurrent(
                                    AppScreen.PublicTimeline(subScreen = subScreen)
                                )
                            }
                        }
                    )

                    is AppScreen.Search -> SearchTopAppBar(
                        drawerState = drawerState,
                        currentSubScreen = currentScreen.subScreen,
                        onCurrentSubScreenChanged = { subScreen ->
                            when (currentScreen.subScreen) {
                                subScreen -> currentScreen.scrollToTop()
                                else -> router.replaceCurrent(
                                    AppScreen.Search(subScreen = subScreen)
                                )
                            }
                        }
                    )

                    else -> TopAppBarWithMenu(
                        title = { Text(res.getScreenTitle(currentScreen)) },
                        drawerState = drawerState
                    )
                }
            }
        },
        bottomBar = {
            Children(routerState = router.state) { screen ->
                val currentScreen = screen.configuration
                MainBottomNavigation(
                    currentScreen = currentScreen,
                    onScreenSelected = { selectedScreen ->
                        when (currentScreen) {
                            selectedScreen -> selectedScreen.scrollToTop()
                            else -> router.replaceCurrent(selectedScreen)
                        }
                    }
                )
            }
        },
        drawerContent = { drawerState ->
            Children(routerState = router.state) { screen ->
                val currentScreen = screen.configuration
                MainAppDrawer(
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = currentScreen,
                    onScreenSelected = { selectedScreen ->
                        when (currentScreen) {
                            selectedScreen -> selectedScreen.scrollToTop()
                            else -> router.replaceCurrent(selectedScreen)
                        }
                    }
                )
            }
        }
    ) { insets ->
        Children(routerState = router.state, animation = crossfadeScale()) { screen ->
            when (val currentScreen = screen.configuration) {
                AppScreen.HomeTimeline -> HomeTimelineScreen(
                    insets = insets,
                    listState = homeListState
                )

                is AppScreen.PublicTimeline -> PublicTimelineScreen(
                    insets = insets,
                    currentSubScreen = currentScreen.subScreen,
                    localListState = publicLocalListState,
                    globalListState = publicGlobalListState
                )

                AppScreen.Notifications -> NotificationsScreen(
                    insets = insets,
                    listState = notificationsListState
                )

                is AppScreen.Search -> SearchScreen(
                    insets = insets,
                    currentSubScreen = currentScreen.subScreen,
                    statusListState = searchStatusesListState,
                    accountsListState = searchAccountsListState,
                    hashtagsListState = searchHashtagsListState
                )

                AppScreen.Account -> AccountScreen(insets = insets)

                AppScreen.Bookmarks -> BookmarksScreen(
                    insets = insets,
                    listState = bookmarksListState
                )

                AppScreen.Favourites -> FavouritesScreen(
                    insets = insets,
                    listState = favouritesListState
                )
            }
        }
    }
}
