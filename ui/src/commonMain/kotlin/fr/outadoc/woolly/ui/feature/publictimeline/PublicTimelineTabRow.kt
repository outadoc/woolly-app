package fr.outadoc.woolly.ui.feature.publictimeline

import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import org.kodein.di.compose.instance

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
