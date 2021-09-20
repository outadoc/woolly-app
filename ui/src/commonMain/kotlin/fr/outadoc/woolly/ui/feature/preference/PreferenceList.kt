package fr.outadoc.woolly.ui.feature.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.account.component.SettingsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    onUpdateSettingsState: (SettingsState) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "App Settings",
            style = MaterialTheme.typography.h5
        )

        LazyColumn {
            item(key = "theme") {
                ListItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Switch app theme"
                        )
                    },
                    text = { Text("Current theme") },
                    secondaryText = { Text(text = settingsState.preferredTheme.toString()) }
                )
            }
        }
    }
}
