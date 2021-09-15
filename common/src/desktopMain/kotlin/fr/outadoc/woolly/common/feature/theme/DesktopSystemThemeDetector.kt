package fr.outadoc.woolly.common.feature.theme

import com.jthemedetecor.OsThemeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class DesktopSystemThemeDetector(appScope: CoroutineScope) : SystemThemeDetector {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val systemTheme: StateFlow<SystemTheme> = callbackFlow {
        if (OsThemeDetector.isSupported()) {
            OsThemeDetector.getDetector().registerListener { isDark ->
                trySend(
                    if (isDark) SystemTheme.DARK else SystemTheme.LIGHT
                )
            }
        } else {
            trySend(SystemTheme.UNSUPPORTED)
        }

    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = getCurrentTheme()
    )

    private fun getCurrentTheme(): SystemTheme {
        if (!OsThemeDetector.isSupported()) return SystemTheme.UNSUPPORTED
        return if (OsThemeDetector.getDetector().isDark) SystemTheme.DARK
        else SystemTheme.LIGHT
    }
}
