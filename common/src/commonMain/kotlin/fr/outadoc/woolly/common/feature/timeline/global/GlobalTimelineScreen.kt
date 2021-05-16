package fr.outadoc.woolly.common.feature.timeline.global

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.navigation.TopAppBarWithMenu
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ResponsiveScaffold
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun GlobalTimelineScreen(
    scaffoldState: ScaffoldState,
    drawer: @Composable ColumnScope.() -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()
    val res by di.instance<AppScreenResources>()

    ResponsiveScaffold(
        scaffoldState = scaffoldState,
        topBar = { disposition ->
            TopAppBarWithMenu(
                title = { Text(res.getScreenTitle(AppScreen.GlobalTimeline)) },
                scaffoldState = scaffoldState,
                disposition = disposition
            )
        },
        bottomBar = { bottomBar() },
        drawerContent = { drawer() }
    ) { insets ->
        Timeline(
            insets = insets,
            pagingSourceFactory = repo::getPublicGlobalTimelineSource
        )
    }
}
