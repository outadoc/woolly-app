package fr.outadoc.woolly.ui.navigation.main

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.ui.common.DrawerMenuButton
import fr.outadoc.woolly.ui.common.ResponsiveScaffold
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineTopAppBar
import fr.outadoc.woolly.ui.feature.search.SearchTopAppBar
import fr.outadoc.woolly.ui.navigation.MainAppDrawer
import fr.outadoc.woolly.ui.navigation.MainBottomNavigation
import fr.outadoc.woolly.ui.navigation.WideAppDrawer
import fr.outadoc.woolly.ui.screen.AppScreenResources
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
                if (component.routerState.value.backStack.isEmpty()) {
                    FloatingActionButton(
                        onClick = { router.push(AppScreen.StatusComposer) }
                    ) {
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
            child.instance.main()
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
