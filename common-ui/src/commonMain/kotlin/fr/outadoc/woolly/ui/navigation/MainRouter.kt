package fr.outadoc.woolly.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.replaceCurrent
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.ui.common.DrawerMenuButton
import fr.outadoc.woolly.ui.common.ResponsiveScaffold
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.account.AccountScreen
import fr.outadoc.woolly.ui.feature.bookmarks.BookmarksScreen
import fr.outadoc.woolly.ui.feature.composer.ComposerScreen
import fr.outadoc.woolly.ui.feature.favourites.FavouritesScreen
import fr.outadoc.woolly.ui.feature.home.HomeTimelineScreen
import fr.outadoc.woolly.ui.feature.media.ImageViewerScreen
import fr.outadoc.woolly.ui.feature.media.toAppImage
import fr.outadoc.woolly.ui.feature.notifications.NotificationsScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineTopAppBar
import fr.outadoc.woolly.ui.feature.search.SearchScreen
import fr.outadoc.woolly.ui.feature.search.SearchTopAppBar
import fr.outadoc.woolly.ui.feature.statusdetails.StatusDetailsScreen
import fr.outadoc.woolly.ui.screen.AppScreen
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.flow.collect
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
            AppScreen.Bookmarks -> bookmarksListState
            AppScreen.Favourites -> favouritesListState
            else -> null
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

    val uriHandler = LocalUriHandler.current
    val onAttachmentClick = { attachment: Attachment ->
        when (attachment) {
            is Attachment.Image -> router.push(
                AppScreen.ImageViewer(image = attachment.toAppImage())
            )
            else -> uriHandler.openUri(attachment.url)
        }
    }

    val onStatusClick = { status: Status ->
        router.push(
            AppScreen.StatusDetails(statusId = status.statusId)
        )
    }

    val statusPoster by di.instance<StatusPoster>()

    scope.launch {
        statusPoster.state.collect { state ->
            with(scaffoldState.snackbarHostState) {
                when {
                    state.posting.isNotEmpty() -> {
                        showSnackbar(
                            message = "Posting status…"
                        )
                    }
                    state.error.isNotEmpty() -> {
                        val result = showSnackbar(
                            message = "Error while posting status",
                            actionLabel = "Retry",
                            SnackbarDuration.Indefinite
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            statusPoster.retryAll()
                        }
                    }
                    else -> currentSnackbarData?.dismiss()
                }
            }
        }
    }

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

                    else -> TopAppBar(
                        modifier = Modifier.height(WoollyDefaults.AppBarHeight),
                        title = { Text(res.getScreenTitle(currentScreen)) },
                        navigationIcon = when {
                            router.state.value.backStack.isNotEmpty() -> {
                                @Composable {
                                    IconButton(onClick = { router.pop() }) {
                                        Icon(Icons.Default.ArrowBack, "Go back")
                                    }
                                }
                            }
                            drawerState != null -> {
                                @Composable { DrawerMenuButton(drawerState) }
                            }
                            else -> null
                        }
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
        narrowDrawerContent = { drawerState ->
            Children(routerState = router.state) { screen ->
                val currentScreen = screen.configuration
                MainAppDrawer(
                    scope = scope,
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
        },
        wideDrawerContent = {
            Children(routerState = router.state) { screen ->
                val currentScreen = screen.configuration
                WideAppDrawer(
                    scope = scope,
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
        },
        floatingActionButton = {
            Children(routerState = router.state) {
                if (router.state.value.backStack.isEmpty()) {
                    FloatingActionButton(
                        onClick = { router.push(AppScreen.StatusComposer) }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Compose a new status")
                    }
                }
            }
        }
    ) { insets ->
        Children(routerState = router.state, animation = crossfadeScale()) { screen ->
            when (val currentScreen = screen.configuration) {
                AppScreen.HomeTimeline -> HomeTimelineScreen(
                    insets = insets,
                    listState = homeListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                is AppScreen.PublicTimeline -> PublicTimelineScreen(
                    insets = insets,
                    currentSubScreen = currentScreen.subScreen,
                    localListState = publicLocalListState,
                    globalListState = publicGlobalListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                AppScreen.Notifications -> NotificationsScreen(
                    insets = insets,
                    listState = notificationsListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                is AppScreen.Search -> SearchScreen(
                    insets = insets,
                    currentSubScreen = currentScreen.subScreen,
                    statusListState = searchStatusesListState,
                    accountsListState = searchAccountsListState,
                    hashtagsListState = searchHashtagsListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                AppScreen.Account -> AccountScreen(insets = insets)

                AppScreen.Bookmarks -> BookmarksScreen(
                    insets = insets,
                    listState = bookmarksListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                AppScreen.Favourites -> FavouritesScreen(
                    insets = insets,
                    listState = favouritesListState,
                    onStatusClick = onStatusClick,
                    onAttachmentClick = onAttachmentClick
                )

                is AppScreen.StatusDetails -> StatusDetailsScreen(
                    statusId = currentScreen.statusId
                )

                is AppScreen.ImageViewer -> ImageViewerScreen(
                    image = currentScreen.image
                )

                AppScreen.StatusComposer -> ComposerScreen(
                    onDismiss = { router.pop() }
                )
            }.let {}
        }
    }
}
