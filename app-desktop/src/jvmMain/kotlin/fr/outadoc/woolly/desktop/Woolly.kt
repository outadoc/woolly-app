@file:JvmName("Woolly")

package fr.outadoc.woolly.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import fr.outadoc.woolly.common.feature.authrouter.component.AuthRouterComponent
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.desktop.inject.DesktopAppDI
import fr.outadoc.woolly.ui.WoollyApp
import org.kodein.di.compose.LocalDI
import org.kodein.di.compose.withDI
import org.kodein.di.direct

fun main() = application {
    DesktopApp()
}

@Composable
private fun ApplicationScope.DesktopApp() = withDI(DesktopAppDI) {
    val di = LocalDI.current
    val mainRouterComponent = remember(di) {
        MainRouterComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            directDI = di.direct
        )
    }

    val authRouterComponent = remember(di) {
        AuthRouterComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            directDI = di.direct
        )
    }

    CompositionLocalProvider(
        LocalUriHandler provides DesktopUriHandler()
    ) {
        val windowState = rememberWindowState(
            size = DpSize(width = 620.dp, height = 900.dp)
        )

        Window(
            title = "Woolly",
            state = windowState,
            onCloseRequest = ::exitApplication
        ) {
            WoollyApp(
                mainRouterComponent = mainRouterComponent,
                authRouterComponent = authRouterComponent
            )
        }
    }
}
