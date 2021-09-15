package fr.outadoc.woolly.common.feature.theme

import android.content.res.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidSystemThemeDetector(appScope: CoroutineScope) : SystemThemeDetector {

    private val _systemTheme = MutableStateFlow(SystemTheme.UNSUPPORTED)
    override val systemTheme: StateFlow<SystemTheme> = _systemTheme.asStateFlow()

    fun notifyConfigurationChanged(configuration: Configuration) {
        val newTheme = when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> SystemTheme.LIGHT
            Configuration.UI_MODE_NIGHT_YES -> SystemTheme.DARK
            else -> SystemTheme.UNSUPPORTED
        }

        _systemTheme.value = newTheme
    }
}
