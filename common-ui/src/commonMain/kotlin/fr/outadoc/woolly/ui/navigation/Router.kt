package fr.outadoc.woolly.ui.navigation

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.preference.AppPreferences
import fr.outadoc.woolly.ui.feature.auth.AuthRouter

@Composable
fun Router(
    appPrefs: AppPreferences,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    when (appPrefs.authenticationState.activeAccount) {
        null -> AuthRouter()
        else -> MainRouter(appPrefs.colorScheme, onColorSchemeChanged)
    }
}
