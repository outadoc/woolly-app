package fr.outadoc.woolly.common.feature.theme

import android.content.res.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidSystemThemeDetector : SystemThemeDetector {

    private val _systemTheme = MutableStateFlow(SystemTheme.Unsupported)
    override val systemTheme: StateFlow<SystemTheme> = _systemTheme.asStateFlow()

    fun notifyConfigurationChanged(configuration: Configuration) {
        val newTheme = when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> SystemTheme.Light
            Configuration.UI_MODE_NIGHT_YES -> SystemTheme.Dark
            else -> SystemTheme.Unsupported
        }

        _systemTheme.value = newTheme
    }
}
