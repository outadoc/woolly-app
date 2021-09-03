package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.feature.mainrouter.component.Content
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.ui.common.DrawerMenuButton
import fr.outadoc.woolly.ui.common.ResponsiveScaffold
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.account.AccountScreen
import fr.outadoc.woolly.ui.feature.bookmarks.BookmarksScreen
import fr.outadoc.woolly.ui.feature.composer.ComposerScreen
import fr.outadoc.woolly.ui.feature.favourites.FavouritesScreen
import fr.outadoc.woolly.ui.feature.home.HomeTimelineScreen
import fr.outadoc.woolly.ui.feature.media.ImageViewerScreen
import fr.outadoc.woolly.ui.feature.notifications.NotificationsScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineTopAppBar
import fr.outadoc.woolly.ui.feature.search.SearchScreen
import fr.outadoc.woolly.ui.feature.search.SearchTopAppBar
import fr.outadoc.woolly.ui.feature.statusdetails.StatusDetailsScreen
import fr.outadoc.woolly.ui.navigation.MainAppDrawer
import fr.outadoc.woolly.ui.navigation.MainBottomNavigation
import fr.outadoc.woolly.ui.navigation.WideAppDrawer
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainRouter(
    component: MainRouterComponent,
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    val res by instance<AppScreenResources>()
    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(component.events) {
        component.events.collect { event ->
            when (event) {
                is MainRouterComponent.Event.OpenUri -> uriHandler.openUri(event.uri)
            }
        }
    }

    PostingStatusSnackbar(
        showPostingSnackbar = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Posting status…",
                    duration = SnackbarDuration.Short
                )
            }
        },
        showErrorSnackbar = { onRetry: () -> Unit ->
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "Error while posting status",
                    actionLabel = "Retry",
                    duration = SnackbarDuration.Indefinite
                )

                if (result == SnackbarResult.ActionPerformed) {
                    onRetry()
                }
            }
        }
    )

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            Children(routerState = component.routerState) { screen ->
                when (val currentScreen = screen.configuration) {
                    is AppScreen.PublicTimeline -> PublicTimelineTopAppBar(
                        title = { Text(res.getScreenTitle(currentScreen)) },
                        drawerState = drawerState,
                        currentSubScreen = currentScreen.subScreen,
                        onSubScreenSelected = { subScreen ->
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
                        onSubScreenSelected = { subScreen ->
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
                        title = { Text(res.getScreenTitle(screen)) },
                        navigationIcon = when {
                            component.shouldDisplayBackButton.value -> {
                                @Composable {
                                    IconButton(onClick = component::onBackPressed) {
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
            Children(routerState = component.routerState) { screen ->
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
            Children(routerState = component.routerState) { screen ->
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
            Children(routerState = component.routerState) { screen ->
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
            Children(routerState = component.routerState) {
                if (component.shouldDisplayComposeButton.value) {
                    FloatingActionButton(onClick = component::onComposeStatusClicked) {
                        Icon(Icons.Default.Edit, contentDescription = "Compose a new status")
                    }
                }
            }
        }
    ) { insets ->
        Children(
            routerState = component.routerState,
            animation = crossfadeScale()
        ) { child ->
            when (val content = child.instance) {
                is Content.HomeTimeline -> HomeTimelineScreen(
                    component = content.component,
                    insets = insets,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.PublicTimeline -> PublicTimelineScreen(
                    component = content.component,
                    insets = insets,
                    currentSubScreen = content.configuration.subScreen,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.Notifications -> NotificationsScreen(
                    component = content.component,
                    insets = insets,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.Search -> SearchScreen(
                    component = content.component,
                    insets = insets,
                    currentSubScreen = content.configuration.subScreen,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.Account -> AccountScreen(
                    component = content.component,
                    insets = insets
                )
                is Content.Bookmarks -> BookmarksScreen(
                    component = content.component,
                    insets = insets,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.Favourites -> FavouritesScreen(
                    component = content.component,
                    insets = insets,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.StatusDetails -> StatusDetailsScreen(
                    component = content.component,
                    insets = insets,
                    statusId = content.configuration.statusId,
                    onStatusClick = component::onStatusClick,
                    onAttachmentClick = component::onAttachmentClick
                )
                is Content.ImageViewer -> ImageViewerScreen(
                    component = content.component,
                    image = content.configuration.image
                )
                is Content.StatusComposer -> ComposerScreen(
                    component = content.component,
                    onDismiss = component::onComposerDismissed
                )
            }
        }
    }
}

@Composable
fun PostingStatusSnackbar(
    showPostingSnackbar: () -> Unit,
    showErrorSnackbar: (() -> Unit) -> Unit
) {
    val statusPoster by instance<StatusPoster>()
    val state by statusPoster.state.collectAsState()

    when (state) {
        StatusPoster.State.Posting -> showPostingSnackbar()
        StatusPoster.State.Error -> showErrorSnackbar {
            statusPoster.retryAll()
        }
        else -> {
        }
    }
}
