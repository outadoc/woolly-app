package fr.outadoc.woolly.common.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.woolly.common.feature.auth.ui.CodeInputScreen
import fr.outadoc.woolly.common.feature.auth.ui.DomainSelectScreen
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun AuthRouter() {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.authState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Welcome to Woolly")
            })
        }
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            when (val state = authState) {
                is AuthState.Disconnected -> DomainSelectScreen(
                    state.loading
                )
                is AuthState.InstanceSelected -> CodeInputScreen(state.error)
                is AuthState.Authenticated -> Unit
            }
        }
    }
}
