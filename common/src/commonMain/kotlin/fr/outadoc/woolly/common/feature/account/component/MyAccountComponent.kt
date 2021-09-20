package fr.outadoc.woolly.common.feature.account.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.woolly.common.LoadState
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.preference.AppPreferences
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyAccountComponent(
    componentContext: ComponentContext,
    accountRepository: AccountRepository,
    private val preferenceRepository: PreferenceRepository
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
}
