package fr.outadoc.woolly.common.feature.timeline.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun PublicTimelineScreen(
    currentSubScreen: PublicTimelineSubScreen,
    onCurrentSubScreenChanged: (PublicTimelineSubScreen) -> Unit,
    localPagingItems: LazyPagingItems<Status>,
    localListState: LazyListState,
    globalPagingItems: LazyPagingItems<Status>,
    globalListState: LazyListState,
    drawer: @Composable ColumnScope.(DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            PublicTimelineTopAppBar(
                title = { Text(res.getScreenTitle(AppScreen.PublicTimeline)) },
                drawerState = drawerState,
                currentSubScreen = currentSubScreen,
                onCurrentSubScreenChanged = onCurrentSubScreenChanged
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawerState -> drawer(drawerState) }
    ) { insets ->
        when (currentSubScreen) {
            is PublicTimelineSubScreen.Local -> Timeline(
                insets = insets,
                lazyPagingItems = localPagingItems,
                lazyListState = localListState
            )
            is PublicTimelineSubScreen.Global -> Timeline(
                insets = insets,
                lazyPagingItems = globalPagingItems,
                lazyListState = globalListState
            )
        }
    }
}
