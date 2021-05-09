package fr.outadoc.woolly.common.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Public

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen) = when (screen) {
        AppScreen.LocalTimeline -> "Local Timeline"
        AppScreen.GlobalTimeline -> "Public Timeline"
    }

    fun getScreenIcon(screen: AppScreen) = when (screen) {
        AppScreen.LocalTimeline -> Icons.Default.LocalLibrary
        AppScreen.GlobalTimeline -> Icons.Default.Public
    }
}
