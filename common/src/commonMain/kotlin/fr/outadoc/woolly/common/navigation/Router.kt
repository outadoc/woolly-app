package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.auth.AuthToken
import fr.outadoc.mastodonk.auth.AuthTokenProvider
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.auth.AuthRouter
import fr.outadoc.woolly.common.feature.auth.AuthState
import fr.outadoc.woolly.common.feature.auth.AuthViewModel
import fr.outadoc.woolly.common.feature.search.repository.SearchRepository
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.ui.ColorScheme
import org.kodein.di.bindSingleton
import org.kodein.di.compose.LocalDI
import org.kodein.di.compose.subDI
import org.kodein.di.instance

@Composable
fun Router(
    colorScheme: ColorScheme,
    onColorSchemeChanged: (ColorScheme) -> Unit
) {
    val di = LocalDI.current
    val vm by di.instance<AuthViewModel>()
    val authState by vm.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Authenticated -> {
            subDI(diBuilder = {
                bindSingleton { StatusRepository(instance()) }
                bindSingleton { SearchRepository(instance()) }
                bindSingleton {
                    MastodonClient {
                        domain = state.authInfo.domain
                        authTokenProvider = AuthTokenProvider {
                            with(state.authInfo.token) {
                                AuthToken(
                                    type = tokenType,
                                    accessToken = accessToken
                                )
                            }
                        }
                    }
                }
            }) {
                MainRouter(colorScheme, onColorSchemeChanged)
            }
        }
        else -> AuthRouter()
    }
}
