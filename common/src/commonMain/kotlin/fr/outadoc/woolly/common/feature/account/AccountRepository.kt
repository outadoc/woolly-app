package fr.outadoc.woolly.common.feature.account

import fr.outadoc.mastodonk.api.entity.Account
import kotlinx.coroutines.flow.StateFlow

interface AccountRepository {
    val currentAccount: StateFlow<Account?>
}
