package fr.outadoc.woolly.common.feature.account.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.paging.api.endpoint.accounts.getStatusesSource
import fr.outadoc.woolly.common.LoadState
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.feature.navigation.tryScrollToTop
import fr.outadoc.woolly.common.feature.preference.AppPreferences
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.state.consumeListStateOrDefault
import fr.outadoc.woolly.common.feature.state.registerListState
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyAccountComponent(
    componentContext: ComponentContext,
    accountRepository: AccountRepository,
    statusPagingRepository: StatusPagingRepository,
    private val preferenceRepository: PreferenceRepository
) : ComponentContext by componentContext, ScrollableComponent {

    val componentScope = getScope()
    val listState = stateKeeper.consumeListStateOrDefault()

    init {
        stateKeeper.registerListState { listState }
    }

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

    val timelinePagingItems: Flow<PagingData<Status>> =
        accountRepository
            .currentAccount
            .mapNotNull { account -> account?.accountId }
            .flatMapLatest { accountId ->
                statusPagingRepository.getPagingData { client ->
                    client.accounts.getStatusesSource(accountId)
                }
            }
            .cachedIn(componentScope)

    val settingsState: StateFlow<SettingsState> =
        preferenceRepository
            .preferences
            .filterIsInstance<LoadState.Loaded<AppPreferences>>()
            .map { preferences ->
                SettingsState(
                    preferredTheme = preferences.value.preferredTheme
                )
            }
            .stateIn(
                scope = componentScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = SettingsState()
            )

    fun onUpdateSettingsState(settingsState: SettingsState) {
        componentScope.launch {
            preferenceRepository.updatePreferences { prefs ->
                prefs.copy(
                    preferredTheme = settingsState.preferredTheme
                )
            }
        }
    }

    override suspend fun scrollToTop(currentConfig: AppScreen?) {
        listState.tryScrollToTop()
    }
}
