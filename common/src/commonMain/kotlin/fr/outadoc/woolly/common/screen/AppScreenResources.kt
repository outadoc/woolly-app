package fr.outadoc.woolly.common.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen) = when (screen) {
        AppScreen.HomeTimeline -> "Home"
        AppScreen.PublicTimeline -> "Public Timeline"
        AppScreen.Notifications -> "Notifications"
        AppScreen.Search -> "Search"
        AppScreen.Account -> "My Profile"
    }

    fun getScreenIcon(screen: AppScreen) = when (screen) {
        AppScreen.HomeTimeline -> Icons.Default.Home
        AppScreen.PublicTimeline -> Icons.Default.Public
        AppScreen.Notifications -> Icons.Default.Notifications
        AppScreen.Search -> Icons.Default.Search
        AppScreen.Account -> Icons.Default.AccountCircle
    }
}
