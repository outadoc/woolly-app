package fr.outadoc.woolly.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import fr.outadoc.woolly.desktop.inject.DesktopAppDI
import fr.outadoc.woolly.ui.WoollyApp
import fr.outadoc.woolly.ui.navigation.main.MainRouterComponent
import org.kodein.di.compose.LocalDI
import org.kodein.di.compose.withDI

fun main() = application {
    DesktopApp()
}

@Composable
private fun ApplicationScope.DesktopApp() = withDI(DesktopAppDI) {
    val di = LocalDI.current
    val mainRouterComponent = remember(di) {
        MainRouterComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            di = di
        )
    }

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
            WoollyApp(
                mainRouterComponent = mainRouterComponent
            )
        }
    }
}
