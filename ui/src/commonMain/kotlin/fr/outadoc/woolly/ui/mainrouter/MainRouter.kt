package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import fr.outadoc.woolly.common.feature.mainrouter.component.MainContent
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.PaddedTopAppBar
import fr.outadoc.woolly.ui.common.takeBottom
import fr.outadoc.woolly.ui.common.takeTop
import fr.outadoc.woolly.ui.feature.notifications.NotificationsTopAppBar
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineTopAppBar
import fr.outadoc.woolly.ui.feature.search.SearchTopAppBar
import fr.outadoc.woolly.ui.screen.getTitle
import fr.outadoc.woolly.ui.strings.stringResource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainRouter(
    component: MainRouterComponent,
    systemInsets: PaddingValues = PaddingValues()
) {
    val scaffoldState = rememberScaffoldState()
    val settingsSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(component.events) {
        component.events.collect { event ->
            when (event) {
                is MainRouterComponent.Event.OpenUri -> uriHandler.openUri(event.uri)
            }
        }
    }

    val postingErrorActionMessage = stringResource(MR.strings.statusComposer_postingError_message)
    val retryActionLabel = stringResource(MR.strings.all_genericError_retry_action)
    val postingSnackbarMessage = stringResource(MR.strings.statusComposer_posting_title)

    PostingStatusSnackbar(
        showPostingSnackbar = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = postingSnackbarMessage,
                    duration = SnackbarDuration.Short
                )
            }
        },
        showErrorSnackbar = { onRetry: () -> Unit ->
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = postingErrorActionMessage,
                    actionLabel = retryActionLabel,
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
            Box(
                modifier = Modifier.clickable(
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
                            contentPadding = systemInsets.takeTop(),
                            component = currentScreen.component,
                            drawerState = drawerState
                        )

                        is MainContent.Notifications -> NotificationsTopAppBar(
                            contentPadding = systemInsets.takeTop(),
                            component = currentScreen.component,
                            drawerState = drawerState
                        )

                        is MainContent.Search -> SearchTopAppBar(
                            contentPadding = systemInsets.takeTop(),
                            component = currentScreen.component,
                            drawerState = drawerState
                        )

                        else -> {
                            PaddedTopAppBar(
                                contentPadding = systemInsets.takeTop(),
                                title = {
                                    Text(text = screen.configuration.getTitle())
                                },
                                navigationIcon = when {
                                    component.shouldDisplayBackButton.value -> {
                                        @Composable {
                                            IconButton(onClick = component::onBackPressed) {
                                                Icon(
                                                    Icons.Default.ArrowBack,
                                                    contentDescription = stringResource(MR.strings.all_back_cd)
                                                )
                                            }
                                        }
                                    }
                                    drawerState != null -> {
                                        @Composable { DrawerMenuButton(drawerState) }
                                    }
                                    else -> null
                                },
                                actions = {
                                    if (currentScreen is MainContent.MyAccount) {
                                        IconButton(
                                            onClick = {
                                                scope.launch { settingsSheetState.show() }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Settings,
                                                contentDescription = stringResource(MR.strings.settings_title)
                                            )
                                        }
                                    }
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
                    },
                    contentPadding = systemInsets.takeBottom()
                )
            }
        },
        narrowDrawerContent = { drawerState ->
            Children(routerState = component.routerState) { screen ->
                MainOverlayDrawer(
                    currentScreen = screen.configuration,
                    onScreenSelected = { target ->
                        scope.launch {
                            component.onScreenSelected(target)
                        }
                    },
                    contentPadding = systemInsets,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        },
        wideDrawerContent = {
            Children(routerState = component.routerState) { screen ->
                MainSideNavigation(
                    modifier = Modifier.padding(systemInsets),
                    currentScreen = screen.configuration,
                    onScreenSelected = { target ->
                        scope.launch {
                            component.onScreenSelected(target)
                        }
                    },
                    scope = scope
                )
            }
        },
        floatingActionButton = {
            val shouldDisplay by component.shouldDisplayComposeButton.subscribeAsState()
            if (shouldDisplay) {
                FloatingActionButton(onClick = component::onComposeStatusClicked) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(MR.strings.navigation_composeStatus_action)
                    )
                }
            }
        },
        drawerGesturesEnabled = true
    ) { insets ->
        Children(
            routerState = component.routerState,
            animation = crossfadeScale()
        ) { child ->
            val genericError = stringResource(MR.strings.all_genericError_title)

            MainRouterChild(
                content = child.instance,
                insets = insets,
                settingsSheetState = settingsSheetState,
                onStatusClick = component::onStatusClick,
                onAttachmentClick = component::onAttachmentClick,
                onStatusReplyClick = component::onStatusReplyClick,
                onAccountClick = component::onAccountClick,
                onComposerDismissed = component::onComposerDismissed,
                onHashtagClick = component::onHashtagClick,
                onLoadError = { _, onRetry ->
                    scope.launch {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = genericError,
                            actionLabel = retryActionLabel,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            onRetry()
                        }
                    }
                }
            )
        }
    }
}
