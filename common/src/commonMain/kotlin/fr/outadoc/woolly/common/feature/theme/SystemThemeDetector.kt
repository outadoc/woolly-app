package fr.outadoc.woolly.common.feature.theme

import kotlinx.coroutines.flow.StateFlow

interface SystemThemeDetector {
    val systemTheme: StateFlow<SystemTheme>
}
