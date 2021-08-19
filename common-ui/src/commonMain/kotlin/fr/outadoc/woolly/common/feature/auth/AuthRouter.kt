package fr.outadoc.woolly.common.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.auth.ui.CodeInputScreen
import fr.outadoc.woolly.common.feature.auth.ui.DomainSelectScreen
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun AuthRouter() {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.state.collectAsState()

    when (val state = authState) {
        is AuthViewModel.State.Disconnected -> DomainSelectScreen(state)
        is AuthViewModel.State.InstanceSelected -> CodeInputScreen(state)
        is AuthViewModel.State.Authenticated -> Unit
    }
}
