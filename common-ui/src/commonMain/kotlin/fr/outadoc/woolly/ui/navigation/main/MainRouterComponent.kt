package fr.outadoc.woolly.ui.navigation.main

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.paging.PagingConfig
import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.common.feature.media.toAppImage
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.ui.common.DrawerMenuButton
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.feature.account.AccountScreen
import fr.outadoc.woolly.ui.feature.bookmarks.BookmarksScreen
import fr.outadoc.woolly.ui.feature.composer.ComposerScreen
import fr.outadoc.woolly.ui.feature.favourites.FavouritesScreen
import fr.outadoc.woolly.ui.feature.home.HomeTimelineScreen
import fr.outadoc.woolly.ui.feature.media.ImageViewerScreen
import fr.outadoc.woolly.ui.feature.notifications.NotificationsScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.woolly.ui.feature.search.SearchScreen
import fr.outadoc.woolly.ui.feature.statusdetails.StatusDetailsScreen
import fr.outadoc.woolly.ui.navigation.Content
import fr.outadoc.woolly.ui.navigation.asContent
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

class MainRouterComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val router = router<AppScreen, Content>(
        initialConfiguration = { AppScreen.HomeTimeline },
        handleBackButton = true,
        childFactory = ::createChild
    )

    val routerState: Value<RouterState<AppScreen, Content>> = router.state

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

    private fun createChild(config: AppScreen, componentContext: ComponentContext): Content =
        when (config) {
            AppScreen.HomeTimeline ->
                homeTimeline(componentContext).asContent(
                    topBar = { StandardTopAppBar(config) }
                ) {
                    HomeTimelineScreen(
                        component = it,
                        insets = insets,
                        listState = homeListState,
                        onStatusClick = onStatusClick,
                        onAttachmentClick = onAttachmentClick
                    )
                }

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
                insets = insets,
                statusId = currentScreen.statusId,
                onStatusClick = onStatusClick,
                onAttachmentClick = onAttachmentClick
            )

            is AppScreen.ImageViewer -> ImageViewerScreen(
                image = currentScreen.image
            )

            AppScreen.StatusComposer -> ComposerScreen(
                onDismiss = { router.pop() }
            )
        }

    private fun homeTimeline(componentContext: ComponentContext): HomeTimelineComponent {
        val componentScope by instance<CoroutineScope>()
        val clientProvider by instance<MastodonClientProvider>()
        val pagingConfig by instance<PagingConfig>()
        val statusActionRepository by instance<StatusActionRepository>()

        return HomeTimelineComponent(
            componentContext = componentContext,
            componentScope = componentScope,
            clientProvider = clientProvider,
            pagingConfig = pagingConfig,
            statusActionRepository = statusActionRepository
        )
    }

    @Composable
    fun StandardTopAppBar(
        screen: AppScreen,
        drawerState: DrawerState?
    ) {
        val res by instance<AppScreenResources>()
        TopAppBar(
            modifier = Modifier.height(WoollyDefaults.AppBarHeight),
            title = { Text(res.getScreenTitle(screen)) },
            navigationIcon = when {
                router.state.value.backStack.isNotEmpty() -> {
                    @Composable {
                        IconButton(onClick = router::pop) {
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

