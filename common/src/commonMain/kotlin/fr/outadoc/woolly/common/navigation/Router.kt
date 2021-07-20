package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.auth.AuthRouter
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationState
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateSupplier
import fr.outadoc.woolly.common.ui.ColorScheme
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun Router(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    val di = LocalDI.current
    val authenticationStateSupplier by di.instance<AuthenticationStateSupplier>()
    val authenticationState by authenticationStateSupplier.state.collectAsState(
        AuthenticationState()
    )

    when (authenticationState.activeAccount) {
        null -> AuthRouter()
        else -> MainRouter(colorScheme, onColorSchemeChanged)
    }
}
