package fr.outadoc.woolly.common.feature.timeline.global

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.navigation.MainTopAppBar
import fr.outadoc.woolly.common.screen.AppScreen
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun GlobalTimelineScreen(
    drawer: @Composable ColumnScope.() -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val di = LocalDI.current
    val repo by di.instance<StatusRepository>()
    val res by di.instance<AppScreenResources>()

    Scaffold(
        topBar = { MainTopAppBar(res.getScreenTitle(AppScreen.GlobalTimeline)) },
        bottomBar = { bottomBar() },
        drawerContent = { drawer() }
    ) { insets ->
        Timeline(
            insets = insets,
            pagingSourceFactory = repo::getPublicGlobalTimelineSource
        )
    }
}
