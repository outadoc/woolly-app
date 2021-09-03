package fr.outadoc.woolly.common.feature.navigation

import fr.outadoc.woolly.common.screen.AppScreen

interface ScrollableComponent {
    suspend fun scrollToTop(currentConfig: AppScreen? = null)
}
