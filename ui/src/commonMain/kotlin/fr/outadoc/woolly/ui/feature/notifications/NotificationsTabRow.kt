package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.notifications.NotificationsSubScreen

@Composable
fun NotificationsTabRow(
    currentSubScreen: NotificationsSubScreen,
    onCurrentSubScreenChanged: (NotificationsSubScreen) -> Unit,
) {
    val tabs = listOf(
        NotificationsSubScreen.All,
        NotificationsSubScreen.MentionsOnly
    )

    TabRow(selectedTabIndex = tabs.indexOf(currentSubScreen)) {
        tabs.forEach { screen ->
            Tab(
                modifier = Modifier.height(48.dp),
                selected = currentSubScreen == screen,
                onClick = { onCurrentSubScreenChanged(screen) }
            ) {
                Text(text = screen.getTitle())
            }
        }
    }
}
