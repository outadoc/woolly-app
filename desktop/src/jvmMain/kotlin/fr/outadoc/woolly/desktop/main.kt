package fr.outadoc.woolly.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import fr.outadoc.woolly.common.App
import fr.outadoc.woolly.common.feature.preference.PreferenceFileProvider
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.preference.PreferenceRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    DesktopApp()
}

@OptIn(DelicateCoroutinesApi::class)
private val di = DI {
    bindSingleton<CoroutineScope> { GlobalScope }

    bindSingleton<PreferenceRepository> {
        PreferenceRepositoryImpl(
            prefs = PreferenceDataStoreFactory.create {
                PreferenceFileProvider.preferenceFile!!
            },
            json = Json.Default
        )
    }
}

@ExperimentalComposeUiApi
@Composable
private fun DesktopApp() = withDI(di) {
    CompositionLocalProvider(
        LocalUriHandler provides DesktopUriHandler()
    ) {
        val windowState = rememberWindowState(
            size = WindowSize(width = 1024.dp, height = 900.dp)
        )

        Window(title = "Woolly", state = windowState) {
            App()
        }
    }
}
