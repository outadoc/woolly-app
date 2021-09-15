package fr.outadoc.woolly.common.feature.theme

import fr.outadoc.woolly.common.LoadState
import fr.outadoc.woolly.common.PreferredTheme
import fr.outadoc.woolly.common.feature.preference.AppPreferences
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class ThemeProvider(
    appScope: CoroutineScope,
    preferenceRepository: PreferenceRepository,
    systemThemeDetector: SystemThemeDetector
) {
    private val fallbackTheme: DisplayTheme = DisplayTheme.Dark

    @OptIn(FlowPreview::class)
    val theme: StateFlow<DisplayTheme> =
        preferenceRepository.preferences
            .filterIsInstance<LoadState.Loaded<AppPreferences>>()
            .flatMapMerge { prefs ->
                when (prefs.value.preferredTheme) {
                    PreferredTheme.FollowSystem ->
                        systemThemeDetector.systemTheme.map { systemTheme ->
                            when (systemTheme) {
                                SystemTheme.Light -> DisplayTheme.Light
                                SystemTheme.Dark -> DisplayTheme.Dark
                                SystemTheme.Unsupported -> fallbackTheme
                            }
                        }
                    PreferredTheme.Light -> flowOf(DisplayTheme.Light)
                    PreferredTheme.Dark -> flowOf(DisplayTheme.Dark)
                }
            }
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = fallbackTheme
            )
}
