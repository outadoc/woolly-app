package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.ui.navigation.TopAppBarWithMenu
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun PublicTimelineTopAppBar(
    component: PublicTimelineComponent,
    drawerState: DrawerState?
) {
    val res by instance<AppScreenResources>()
    val state by component.state.collectAsState()

    val scope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Column {
            TopAppBarWithMenu(
                title = { Text(text = res.getScreenTitle(AppScreen.PublicTimeline)) },
                backgroundColor = MaterialTheme.colors.primarySurface,
                drawerState = drawerState,
                elevation = 0.dp
            )

            PublicTimelineTabRow(
                currentSubScreen = state.subScreen,
                onCurrentSubScreenChanged = { subScreen ->
                    scope.launch {
                        component.onSubScreenSelected(subScreen)
                    }
                }
            )
        }
    }
}

@Composable
fun PublicTimelineTabRow(
    currentSubScreen: PublicTimelineSubScreen,
    onCurrentSubScreenChanged: (PublicTimelineSubScreen) -> Unit,
) {
    val res by instance<PublicTimelineScreenResources>()

    val tabs = listOf(
        PublicTimelineSubScreen.Local,
        PublicTimelineSubScreen.Global
    )

    TabRow(selectedTabIndex = tabs.indexOf(currentSubScreen)) {
        tabs.forEach { screen ->
            Tab(
                modifier = Modifier.height(48.dp),
                selected = currentSubScreen == screen,
                onClick = {
                    onCurrentSubScreenChanged(screen)
                }
            ) {
                Text(text = res.getScreenTitle(screen))
            }
        }
    }
}