package fr.outadoc.woolly.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen) = when (screen) {
        AppScreen.Account -> "My Profile"
        AppScreen.Bookmarks -> "Bookmarks"
        AppScreen.Favourites -> "Favourites"
        AppScreen.HomeTimeline -> "Home"
        AppScreen.Notifications -> "Notifications"
        is AppScreen.PublicTimeline -> "Public Timeline"
        is AppScreen.Search -> "Search"
        is AppScreen.StatusDetails -> "Status"
    }

    fun getScreenIcon(screen: AppScreen) = when (screen) {
        AppScreen.Account -> Icons.Default.AccountCircle
        AppScreen.Bookmarks -> Icons.Default.Bookmarks
        AppScreen.Favourites -> Icons.Default.Favorite
        AppScreen.HomeTimeline -> Icons.Default.Home
        AppScreen.Notifications -> Icons.Default.Notifications
        is AppScreen.PublicTimeline -> Icons.Default.Public
        is AppScreen.Search -> Icons.Default.Search
        is AppScreen.StatusDetails -> Icons.Default.ChatBubble
    }
}
