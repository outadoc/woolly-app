package fr.outadoc.woolly.common.feature.account

import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AccountRepositoryImpl(
    scope: CoroutineScope,
    clientProvider: MastodonClientProvider
) : AccountRepository {

    override val currentAccount: StateFlow<Account?> =
        clientProvider.mastodonClient.map { client ->
            client?.accounts?.verifyCredentials()
        }.stateIn(
            scope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed()
        )
}
