package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.ui.mainrouter.TopAppBarWithMenu
import fr.outadoc.woolly.ui.screen.getTitle
import kotlinx.coroutines.launch

@Composable
fun PublicTimelineTopAppBar(
    modifier: Modifier = Modifier,
    component: PublicTimelineComponent,
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by component.state.collectAsState()
    val scope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            TopAppBarWithMenu(
                modifier = modifier,
                contentPadding = contentPadding,
                title = { Text(text = AppScreen.PublicTimeline.getTitle()) },
                backgroundColor = MaterialTheme.colorScheme.surface,
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
