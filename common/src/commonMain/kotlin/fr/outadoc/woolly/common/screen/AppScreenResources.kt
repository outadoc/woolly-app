package fr.outadoc.woolly.common.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen) = when (screen) {
        AppScreen.HomeTimeline -> "Home"
        AppScreen.LocalTimeline -> "Local Timeline"
        AppScreen.GlobalTimeline -> "Public Timeline"
        AppScreen.Search -> "Search"
        AppScreen.Account -> "My Profile"
    }

    fun getScreenIcon(screen: AppScreen) = when (screen) {
        AppScreen.HomeTimeline -> Icons.Default.Home
        AppScreen.LocalTimeline -> Icons.Default.LocalLibrary
        AppScreen.GlobalTimeline -> Icons.Default.Public
        AppScreen.Search -> Icons.Default.Search
        AppScreen.Account -> Icons.Default.AccountCircle
    }
}
