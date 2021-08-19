package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.feature.auth.AuthRouter
import fr.outadoc.woolly.common.feature.preference.AppPreferences

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
