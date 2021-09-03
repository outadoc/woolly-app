package fr.outadoc.woolly.common.feature.mainrouter.component

import fr.outadoc.woolly.common.screen.AppScreen

interface ScrollableComponent {
    fun scrollToTop(currentConfig: AppScreen? = null)
}
