package fr.outadoc.woolly.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import fr.outadoc.woolly.desktop.inject.DesktopAppDI
import fr.outadoc.woolly.ui.WoollyApp
import org.kodein.di.compose.withDI

fun main() = application {
    DesktopApp()
}

@Composable
private fun ApplicationScope.DesktopApp() = withDI(DesktopAppDI) {
    CompositionLocalProvider(
        LocalUriHandler provides DesktopUriHandler()
    ) {
        val windowState = rememberWindowState(
            size = WindowSize(width = 620.dp, height = 900.dp)
        )

        Window(
            title = "Woolly",
            state = windowState,
            onCloseRequest = ::exitApplication
        ) {
            WoollyApp()
        }
    }
}
