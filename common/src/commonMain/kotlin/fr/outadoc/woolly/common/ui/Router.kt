package fr.outadoc.woolly.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.auth.AuthRouter
import fr.outadoc.woolly.common.feature.auth.AuthState
import fr.outadoc.woolly.common.feature.auth.AuthViewModel
import org.kodein.di.bindSingleton
import org.kodein.di.compose.LocalDI
import org.kodein.di.compose.subDI
import org.kodein.di.instance

@Composable
fun Router(toggleDarkMode: () -> Unit) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Authenticated -> {
            subDI(diBuilder = {
                bindSingleton {
                    MastodonClient {
                        domain = state.authInfo.domain
                    }
                }
            }) {
                MainRouter(toggleDarkMode)
            }
        }
        else -> AuthRouter()
    }
}
