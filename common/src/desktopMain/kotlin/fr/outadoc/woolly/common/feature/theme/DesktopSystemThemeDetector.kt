package fr.outadoc.woolly.common.feature.theme

import com.jthemedetecor.OsThemeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class DesktopSystemThemeDetector(appScope: CoroutineScope) : SystemThemeDetector {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val systemTheme: StateFlow<SystemTheme> = callbackFlow {
        val callback = fun(isDark: Boolean) {
            trySend(
                if (isDark) SystemTheme.Dark else SystemTheme.Light
            )
        }

        if (OsThemeDetector.isSupported()) {
            OsThemeDetector.getDetector().registerListener(callback)
        } else {
            trySend(SystemTheme.Unsupported)
        }

        awaitClose {
            OsThemeDetector.getDetector().removeListener(callback)
        }

    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = getCurrentTheme()
    )

    private fun getCurrentTheme(): SystemTheme {
        if (!OsThemeDetector.isSupported()) return SystemTheme.Unsupported
        return if (OsThemeDetector.getDetector().isDark) SystemTheme.Dark
        else SystemTheme.Light
    }
}
