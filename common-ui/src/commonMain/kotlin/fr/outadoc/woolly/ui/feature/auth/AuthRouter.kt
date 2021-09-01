package fr.outadoc.woolly.ui.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import org.kodein.di.compose.instance

@Composable
fun AuthRouter() {
    val viewModel by instance<AuthViewModel>()
    val authState by viewModel.state.collectAsState()

    when (val state = authState) {
        is AuthViewModel.State.Disconnected -> DomainSelectScreen(state)
        is AuthViewModel.State.InstanceSelected -> CodeInputScreen(state)
        is AuthViewModel.State.Authenticated -> Unit
    }
}
