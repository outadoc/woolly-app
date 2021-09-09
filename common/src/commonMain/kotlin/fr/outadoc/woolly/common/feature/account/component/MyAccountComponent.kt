package fr.outadoc.woolly.common.feature.account.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MyAccountComponent(
    componentContext: ComponentContext,
    accountRepository: AccountRepository
) : ComponentContext by componentContext {

    val componentScope = getScope()

    data class State(
        val account: Account? = null
    )

    val state = accountRepository
        .currentAccount
        .map { account ->
            State(account = account)
        }
        .stateIn(
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = State()
        )
}
