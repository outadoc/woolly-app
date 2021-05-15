package fr.outadoc.woolly.common.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.auth.ui.CodeInputScreen
import fr.outadoc.woolly.common.feature.auth.ui.DomainSelectScreen
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun AuthRouter() {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Disconnected -> DomainSelectScreen(state.error)
        is AuthState.InstanceSelected -> CodeInputScreen(state.error)
        is AuthState.Authenticated -> Unit
    }
}
