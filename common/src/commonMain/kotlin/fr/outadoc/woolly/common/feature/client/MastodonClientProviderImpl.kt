package fr.outadoc.woolly.common.feature.client

import fr.outadoc.mastodonk.auth.AuthToken
import fr.outadoc.mastodonk.auth.AuthTokenProvider
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateSupplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MastodonClientProviderImpl(
    scope: CoroutineScope,
    authenticationStateSupplier: AuthenticationStateSupplier
) : MastodonClientProvider {

    override val mastodonClient: StateFlow<MastodonClient?> =
        authenticationStateSupplier.state.map { state ->
            state.activeAccount?.let { account ->
                MastodonClient {
                    domain = account.domain
                    authTokenProvider = AuthTokenProvider {
                        AuthToken(
                            type = account.token.tokenType,
                            accessToken = account.token.accessToken
                        )
                    }
                }
            }
        }.stateIn(
            scope,
            initialValue = null,
            started = SharingStarted.Eagerly
        )
}
