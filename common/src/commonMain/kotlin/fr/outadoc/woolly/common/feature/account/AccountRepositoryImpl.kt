package fr.outadoc.woolly.common.feature.account

import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.client.MastodonClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountRepositoryImpl(
    scope: CoroutineScope,
    private val mastodonClient: MastodonClient
) : AccountRepository {

    private val _currentAccount = MutableStateFlow<Account?>(null)
    override val currentAccount: StateFlow<Account?>
        get() = _currentAccount

    init {
        scope.launch {
            _currentAccount.value = mastodonClient.accounts.verifyCredentials()
        }
    }
}
