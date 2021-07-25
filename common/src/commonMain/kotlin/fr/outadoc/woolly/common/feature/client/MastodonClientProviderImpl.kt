package fr.outadoc.woolly.common.feature.client

import fr.outadoc.mastodonk.auth.AuthToken
import fr.outadoc.mastodonk.auth.AuthTokenProvider
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.LoadState
import fr.outadoc.woolly.common.feature.preference.AppPreferences
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MastodonClientProviderImpl(
    scope: CoroutineScope,
    preferenceRepository: PreferenceRepository
) : MastodonClientProvider {

    override val mastodonClient: StateFlow<MastodonClient?> =
        preferenceRepository
            .preferences
            .filterIsInstance<LoadState.Loaded<AppPreferences>>()
            .map { it.value.authenticationState.activeAccount }
            .distinctUntilChanged()
            .map { activeAccount ->
                activeAccount?.let { account ->
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
