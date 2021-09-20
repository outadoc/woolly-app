package fr.outadoc.woolly.ui.feature.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.account.component.SettingsState
import fr.outadoc.woolly.ui.common.DropdownMenu
import fr.outadoc.woolly.ui.common.DropdownMenuItem

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
                var expanded by remember { mutableStateOf(false) }

                Box {
                    ListItem(
                        modifier = Modifier.clickable { expanded = !expanded },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = "Switch app theme"
                            )
                        },
                        text = { Text("Current theme") },
                        secondaryText = {
                            Text(
                                text = when (settingsState.preferredTheme) {
                                    PreferredTheme.Light -> "Always light"
                                    PreferredTheme.Dark -> "Always dark"
                                    PreferredTheme.FollowSystem -> "Follow system theme"
                                }
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onUpdateSettingsState(settingsState.copy(preferredTheme = PreferredTheme.FollowSystem))
                                expanded = false
                            }
                        ) {
                            Text("Follow system theme")
                        }

                        DropdownMenuItem(
                            onClick = {
                                onUpdateSettingsState(settingsState.copy(preferredTheme = PreferredTheme.Dark))
                                expanded = false
                            }
                        ) {
                            Text("Always dark")
                        }

                        DropdownMenuItem(
                            onClick = {
                                onUpdateSettingsState(settingsState.copy(preferredTheme = PreferredTheme.Light))
                                expanded = false
                            }
                        ) {
                            Text("Always light")
                        }
                    }
                }
            }
        }
    }
}
