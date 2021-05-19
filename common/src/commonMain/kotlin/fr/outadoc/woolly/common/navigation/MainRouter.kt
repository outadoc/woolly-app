package fr.outadoc.woolly.common.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.repository.SearchRepository
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.search.ui.SearchTopAppBar
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.feature.timeline.ui.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.ui.PublicTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.ui.PublicTimelineTopAppBar
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
    var currentScreen: AppScreen by remember {
        mutableStateOf(AppScreen.HomeTimeline)
    }

    val scope = rememberCoroutineScope()

    val pagingConfig = PagingConfig(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    val di = LocalDI.current
    val searchRepo by di.instance<SearchRepository>()
    val statusRepo by di.instance<StatusRepository>()

    // SEARCH
    var searchTerm by remember { mutableStateOf("") }
    var currentSearchScreen: SearchSubScreen by remember {
        mutableStateOf(SearchSubScreen.Statuses)
    }

    val searchStatusesListState = rememberLazyListState()
    val searchStatusPagingItems = remember(searchTerm) {
        Pager(pagingConfig) { searchRepo.getStatusSearchResultsSource(searchTerm) }
    }.flow.collectAsLazyPagingItems()

    val searchAccountsListState = rememberLazyListState()
    val searchAccountsPagingItems = remember(searchTerm) {
        Pager(pagingConfig) { searchRepo.getAccountSearchResultsSource(searchTerm) }
    }.flow.collectAsLazyPagingItems()

    val searchHashtagsListState = rememberLazyListState()
    val searchHashtagsPagingItems = remember(searchTerm) {
        Pager(pagingConfig) { searchRepo.getHashtagSearchResultsSource(searchTerm) }
    }.flow.collectAsLazyPagingItems()

    // HOME
    val homeListState = rememberLazyListState()
    val homePagingItems = remember {
        Pager(pagingConfig) { statusRepo.getHomeTimelineSource() }
    }.flow.collectAsLazyPagingItems()

    // PUBLIC TIMELINE
    var currentPublicTimelineScreen: PublicTimelineSubScreen by remember {
        mutableStateOf(PublicTimelineSubScreen.Local)
    }

    val publicLocalListState = rememberLazyListState()
    val publicLocalPagingItems = remember {
        Pager(pagingConfig) { statusRepo.getPublicLocalTimelineSource() }
    }.flow.collectAsLazyPagingItems()

    val publicGlobalListState = rememberLazyListState()
    val publicGlobalPagingItems = remember {
        Pager(pagingConfig) { statusRepo.getPublicGlobalTimelineSource() }
    }.flow.collectAsLazyPagingItems()

    fun onScreenSelected(selectedScreen: AppScreen) {
        if (selectedScreen != currentScreen) currentScreen = selectedScreen
        else when (selectedScreen) {
            AppScreen.HomeTimeline -> homeListState
            AppScreen.PublicTimeline -> when (currentPublicTimelineScreen) {
                PublicTimelineSubScreen.Global -> publicGlobalListState
                PublicTimelineSubScreen.Local -> publicLocalListState
            }
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
                    searchTerm = searchTerm,
                    onSearchTermChanged = { searchTerm = it },
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
                    pagingItems = homePagingItems,
                    listState = homeListState,
                )

                AppScreen.PublicTimeline -> PublicTimelineScreen(
                    insets = insets,
                    currentSubScreen = currentPublicTimelineScreen,
                    localPagingItems = publicLocalPagingItems,
                    localListState = publicLocalListState,
                    globalPagingItems = publicGlobalPagingItems,
                    globalListState = publicGlobalListState
                )

                AppScreen.Search -> SearchScreen(
                    insets = insets,
                    searchTerm = searchTerm,
                    currentSubScreen = currentSearchScreen,
                    statusPagingItems = searchStatusPagingItems,
                    statusListState = searchStatusesListState,
                    accountsPagingItems = searchAccountsPagingItems,
                    accountsListState = searchAccountsListState,
                    hashtagsPagingItems = searchHashtagsPagingItems,
                    hashtagsListState = searchHashtagsListState
                )

                AppScreen.Account -> AccountScreen(insets = insets)
            }
        }
    }
}
