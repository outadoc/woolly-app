package fr.outadoc.woolly.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.outadoc.woolly.common.ColorScheme
import fr.outadoc.woolly.common.LoadState
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.ui.common.WoollyTheme
import fr.outadoc.woolly.ui.feature.auth.AuthRouter
import fr.outadoc.woolly.ui.navigation.MainRouter
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@Composable
fun WoollyApp(onFinishedLoading: () -> Unit = {}) {
    val prefs by instance<PreferenceRepository>()
    val appPrefsState by prefs.preferences.collectAsState(initial = LoadState.Loading())

    val scope = rememberCoroutineScope()

    when (val state = appPrefsState) {
        is LoadState.Loaded -> {
            SideEffect {
                onFinishedLoading()
            }

            val appPrefs = state.value
            WoollyTheme(isDarkMode = appPrefs.colorScheme == ColorScheme.Dark) {
                when (appPrefs.authenticationState.activeAccount) {
                    null -> AuthRouter()
                    else -> {
                        MainRouter(
                            colorScheme = appPrefs.colorScheme,
                            onColorSchemeChanged = { colorScheme ->
                                scope.launch {
                                    prefs.updatePreferences { current ->
                                        current.copy(colorScheme = colorScheme)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
        is LoadState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
