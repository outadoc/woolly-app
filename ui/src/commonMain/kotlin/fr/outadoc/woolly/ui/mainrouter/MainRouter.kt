package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.mainrouter.component.MainContent
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineTopAppBar
import fr.outadoc.woolly.ui.feature.search.SearchTopAppBar
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
            Box(modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        scope.launch { component.scrollToTop() }
                    }
                )
            ) {
                Children(routerState = component.routerState) { screen ->
                    when (val currentScreen = screen.instance) {
                        is MainContent.PublicTimeline -> PublicTimelineTopAppBar(
                            component = currentScreen.component,
                            drawerState = drawerState
                        )

                        is MainContent.Search -> SearchTopAppBar(
                            component = currentScreen.component,
                            drawerState = drawerState
                        )

                        else -> {
                            val res by instance<AppScreenResources>()
                            TopAppBar(
                                modifier = Modifier.height(WoollyDefaults.AppBarHeight),
                                title = { Text(res.getScreenTitle(screen.configuration)) },
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
                }
            }
        },
        bottomBar = {
            Children(routerState = component.routerState) { screen ->
                MainBottomNavigation(
                    currentScreen = screen.configuration,
                    onScreenSelected = { target ->
                        scope.launch {
                            component.onScreenSelected(target)
                        }
                    }
                )
            }
        },
        narrowDrawerContent = { drawerState ->
            Children(routerState = component.routerState) { screen ->
                MainOverlayDrawer(
                    scope = scope,
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = screen.configuration,
                    onScreenSelected = { target ->
                        scope.launch {
                            component.onScreenSelected(target)
                        }
                    }
                )
            }
        },
        wideDrawerContent = {
            Children(routerState = component.routerState) { screen ->
                MainSideNavigation(
                    scope = scope,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = screen.configuration,
                    onScreenSelected = { target ->
                        scope.launch {
                            component.onScreenSelected(target)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            val shouldDisplay by component.shouldDisplayComposeButton.subscribeAsState()
            if (shouldDisplay) {
                FloatingActionButton(onClick = component::onComposeStatusClicked) {
                    Icon(Icons.Default.Edit, contentDescription = "Compose a new status")
                }
            }
        }
    ) { insets ->
        Children(
            routerState = component.routerState,
            animation = crossfadeScale()
        ) { child ->
            MainRouterChild(
                content = child.instance,
                insets = insets,
                onStatusClick = component::onStatusClick,
                onAttachmentClick = component::onAttachmentClick,
                onStatusReplyClick = component::onStatusReplyClick,
                onAccountClick = component::onAccountClick,
                onComposerDismissed = component::onComposerDismissed
            )
        }
    }
}
