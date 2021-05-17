package fr.outadoc.woolly.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.auth.AuthToken
import fr.outadoc.mastodonk.auth.AuthTokenProvider
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.account.AccountRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.AuthRouter
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoSupplier
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
    val authInfoSupplier by di.instance<AuthInfoSupplier>()
    val authInfo by authInfoSupplier.authInfo.collectAsState()

    when (val state = authInfo) {
        null -> AuthRouter()
        else -> {
            subDI(diBuilder = {
                bindSingleton { StatusRepository(instance()) }
                bindSingleton { SearchRepository(instance()) }

                bindSingleton<AccountRepository> { AccountRepositoryImpl(instance(), instance()) }

                bindSingleton {
                    MastodonClient {
                        domain = state.domain
                        authTokenProvider = AuthTokenProvider {
                            AuthToken(
                                type = state.token.tokenType,
                                accessToken = state.token.accessToken
                            )
                        }
                    }
                }
            }) {
                MainRouter(colorScheme, onColorSchemeChanged)
            }
        }
    }
}
