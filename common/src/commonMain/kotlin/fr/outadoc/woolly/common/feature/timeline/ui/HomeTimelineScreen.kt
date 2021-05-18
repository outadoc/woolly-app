package fr.outadoc.woolly.common.feature.timeline.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.navigation.TopAppBarWithMenu
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun HomeTimelineScreen(
    pagingItems: LazyPagingItems<Status>,
    listState: LazyListState,
    drawer: @Composable ColumnScope.(DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()

    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            TopAppBarWithMenu(
                title = { Text(res.getScreenTitle(AppScreen.HomeTimeline)) },
                drawerState = drawerState
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawerState -> drawer(drawerState) }
    ) { insets ->
        Timeline(
            insets = insets,
            lazyPagingItems = pagingItems,
            lazyListState = listState
        )
    }
}
