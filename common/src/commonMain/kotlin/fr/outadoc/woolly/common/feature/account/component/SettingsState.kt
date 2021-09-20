package fr.outadoc.woolly.common.feature.account.component

import fr.outadoc.woolly.common.PreferredTheme

data class SettingsState(
    val preferredTheme: PreferredTheme = PreferredTheme.FollowSystem
)
