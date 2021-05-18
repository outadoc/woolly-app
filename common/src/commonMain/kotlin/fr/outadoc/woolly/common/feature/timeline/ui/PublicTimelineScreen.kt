package fr.outadoc.woolly.common.feature.timeline.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.timeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun PublicTimelineScreen(
    drawer: @Composable ColumnScope.(DrawerState?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val di = LocalDI.current
    val res by di.instance<AppScreenResources>()
    var currentSubScreen by remember {
        mutableStateOf<PublicTimelineSubScreen>(PublicTimelineSubScreen.Local)
    }

    val scaffoldState = rememberScaffoldState()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { drawerState ->
            PublicTimelineTopAppBar(
                title = { Text(res.getScreenTitle(AppScreen.PublicTimeline)) },
                drawerState = drawerState,
                currentSubScreen = currentSubScreen,
                onCurrentSubScreenChanged = { currentSubScreen = it }
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawerState -> drawer(drawerState) }
    ) { insets ->
        when (currentSubScreen) {
            is PublicTimelineSubScreen.Local -> LocalTimelineScreen(insets)
            is PublicTimelineSubScreen.Global -> GlobalTimelineScreen(insets)
        }
    }
}


@Composable
fun LocalTimelineScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()

    Timeline(
        insets = insets,
        pagingSourceFactory = repo::getPublicLocalTimelineSource
    )
}


@Composable
fun GlobalTimelineScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()

    Timeline(
        insets = insets,
        pagingSourceFactory = repo::getPublicGlobalTimelineSource
    )
}
