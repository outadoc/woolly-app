package fr.outadoc.woolly.ui.feature.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.account.component.SettingsState
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.DropdownMenu
import fr.outadoc.woolly.ui.common.DropdownMenuItem
import fr.outadoc.woolly.ui.strings.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    onUpdateSettingsState: (SettingsState) -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(MR.strings.settings_title),
            style = MaterialTheme.typography.titleSmall
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
                                contentDescription = stringResource(MR.strings.settings_switchTheme_cd)
                            )
                        },
                        text = { Text(stringResource(MR.strings.settings_currentTheme_title)) },
                        secondaryText = {
                            Text(
                                text = when (settingsState.preferredTheme) {
                                    PreferredTheme.FollowSystem -> stringResource(MR.strings.settings_theme_system)
                                    PreferredTheme.Dark -> stringResource(MR.strings.settings_theme_dark)
                                    PreferredTheme.Light -> stringResource(MR.strings.settings_theme_light)
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
                            Text(stringResource(MR.strings.settings_theme_system))
                        }

                        DropdownMenuItem(
                            onClick = {
                                onUpdateSettingsState(settingsState.copy(preferredTheme = PreferredTheme.Dark))
                                expanded = false
                            }
                        ) {
                            Text(stringResource(MR.strings.settings_theme_dark))
                        }

                        DropdownMenuItem(
                            onClick = {
                                onUpdateSettingsState(settingsState.copy(preferredTheme = PreferredTheme.Light))
                                expanded = false
                            }
                        ) {
                            Text(stringResource(MR.strings.settings_theme_light))
                        }
                    }
                }
            }
        }
    }
}
