package fr.outadoc.woolly.common.feature.navigation

import fr.outadoc.woolly.common.feature.mainrouter.AppScreen

interface ScrollableComponent {
    suspend fun scrollToTop(currentConfig: AppScreen? = null)
}
