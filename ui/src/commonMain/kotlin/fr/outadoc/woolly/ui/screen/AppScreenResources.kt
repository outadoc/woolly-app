package fr.outadoc.woolly.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun AppScreen.getTitle() = when (this) {
    AppScreen.MyAccount -> stringResource(MR.strings.myAccount_title)
    AppScreen.Bookmarks -> stringResource(MR.strings.bookmarks_title)
    AppScreen.Favourites -> stringResource(MR.strings.favourites_title)
    AppScreen.HomeTimeline -> stringResource(MR.strings.home_title)
    AppScreen.Notifications -> stringResource(MR.strings.notifications_title)
    is AppScreen.PublicTimeline -> stringResource(MR.strings.publicTimeline_title)
    is AppScreen.Search -> stringResource(MR.strings.search_title)
    is AppScreen.StatusDetails -> stringResource(MR.strings.statusDetails_title)
    is AppScreen.ImageViewer -> stringResource(MR.strings.imageViewer_title)
    is AppScreen.StatusComposer -> stringResource(MR.strings.statusComposer_title)
    is AppScreen.AccountDetails -> stringResource(MR.strings.accountDetails_title)
    is AppScreen.HashtagTimeline -> stringResource(MR.strings.hashtagTimeline_title, hashtag)
}

fun AppScreen.getIcon() = when (this) {
    AppScreen.MyAccount -> Icons.Default.AccountCircle
    AppScreen.Bookmarks -> Icons.Default.Bookmarks
    AppScreen.Favourites -> Icons.Default.Star
    AppScreen.HomeTimeline -> Icons.Default.Home
    AppScreen.Notifications -> Icons.Default.Notifications
    is AppScreen.PublicTimeline -> Icons.Default.Public
    is AppScreen.Search -> Icons.Default.Search
    is AppScreen.StatusDetails -> Icons.Default.ChatBubble
    is AppScreen.ImageViewer -> Icons.Default.Image
    is AppScreen.StatusComposer -> Icons.Default.Edit
    is AppScreen.AccountDetails -> Icons.Default.AccountCircle
    is AppScreen.HashtagTimeline -> Icons.Default.Tag
}
