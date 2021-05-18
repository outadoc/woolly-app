package fr.outadoc.woolly.common.navigation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import fr.outadoc.woolly.common.feature.account.ui.AccountScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.common.feature.search.repository.SearchRepository
import fr.outadoc.woolly.common.feature.search.ui.SearchScreen
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.feature.timeline.ui.HomeTimelineScreen
import fr.outadoc.woolly.common.feature.timeline.ui.PublicTimelineScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.ui.ColorScheme
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

    val onScreenSelected = { screen: AppScreen ->
        currentScreen = screen
    }

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

    when (currentScreen) {
        AppScreen.HomeTimeline -> HomeTimelineScreen(
            pagingItems = homePagingItems,
            listState = homeListState,
            drawer = { drawerState ->
                MainAppDrawer(
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = currentScreen,
                    onScreenSelected = onScreenSelected
                )
            }
        ) { MainBottomNavigation(currentScreen, onScreenSelected) }

        AppScreen.PublicTimeline -> PublicTimelineScreen(
            currentSubScreen = currentPublicTimelineScreen,
            onCurrentSubScreenChanged = { currentPublicTimelineScreen = it },
            localPagingItems = publicLocalPagingItems,
            localListState = publicLocalListState,
            globalPagingItems = publicGlobalPagingItems,
            globalListState = publicGlobalListState,
            drawer = { drawerState ->
                MainAppDrawer(
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = currentScreen,
                    onScreenSelected = onScreenSelected
                )
            }
        ) { MainBottomNavigation(currentScreen, onScreenSelected) }

        AppScreen.Search -> SearchScreen(
            searchTerm = searchTerm,
            onSearchTermChanged = { searchTerm = it },
            currentSubScreen = currentSearchScreen,
            onCurrentSubScreenChanged = { currentSearchScreen = it },
            statusPagingItems = searchStatusPagingItems,
            statusListState = searchStatusesListState,
            accountsPagingItems = searchAccountsPagingItems,
            accountsListState = searchAccountsListState,
            hashtagsPagingItems = searchHashtagsPagingItems,
            hashtagsListState = searchHashtagsListState,
            drawer = { drawerState ->
                MainAppDrawer(
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = currentScreen,
                    onScreenSelected = onScreenSelected
                )
            },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )

        AppScreen.Account -> AccountScreen(
            drawer = { drawerState ->
                MainAppDrawer(
                    drawerState = drawerState,
                    colorScheme = colorScheme,
                    onColorSchemeChanged = onColorSchemeChanged,
                    currentScreen = currentScreen,
                    onScreenSelected = onScreenSelected
                )
            },
            bottomBar = { MainBottomNavigation(currentScreen, onScreenSelected) }
        )
    }
}
