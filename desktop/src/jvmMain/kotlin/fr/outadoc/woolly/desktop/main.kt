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
import fr.outadoc.woolly.common.WoollyApp
import fr.outadoc.woolly.desktop.inject.DesktopAppDI
import org.kodein.di.compose.withDI

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    DesktopApp()
}

@ExperimentalComposeUiApi
@Composable
private fun DesktopApp() = withDI(DesktopAppDI) {
    CompositionLocalProvider(
        LocalUriHandler provides DesktopUriHandler()
    ) {
        val windowState = rememberWindowState(
            size = WindowSize(width = 1024.dp, height = 900.dp)
        )

        Window(title = "Woolly", state = windowState) {
            WoollyApp()
        }
    }
}
