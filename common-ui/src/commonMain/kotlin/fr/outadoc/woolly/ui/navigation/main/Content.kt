package fr.outadoc.woolly.ui.navigation.main

import fr.outadoc.woolly.common.feature.account.component.AccountComponent
import fr.outadoc.woolly.common.feature.bookmarks.component.BookmarksComponent
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
import fr.outadoc.woolly.common.feature.favourites.component.FavouritesComponent
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.common.feature.media.component.AttachmentViewerComponent
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.common.screen.AppScreen

sealed class Content {

    data class Account(
        val configuration: AppScreen.Account,
        val component: AccountComponent
    ) : Content()

    data class Bookmarks(
        val configuration: AppScreen.Bookmarks,
        val component: BookmarksComponent
    ) : Content()

    data class Favourites(
        val configuration: AppScreen.Favourites,
        val component: FavouritesComponent
    ) : Content()

    data class HomeTimeline(
        val configuration: AppScreen.HomeTimeline,
        val component: HomeTimelineComponent
    ) : Content()

    data class Notifications(
        val configuration: AppScreen.Notifications,
        val component: NotificationsComponent
    ) : Content()

    data class PublicTimeline(
        val configuration: AppScreen.PublicTimeline,
        val component: PublicTimelineComponent
    ) : Content()

    data class Search(
        val configuration: AppScreen.Search,
        val component: SearchComponent
    ) : Content()

    data class StatusDetails(
        val configuration: AppScreen.StatusDetails,
        val component: StatusDetailsComponent
    ) : Content()

    data class ImageViewer(
        val configuration: AppScreen.ImageViewer,
        val component: AttachmentViewerComponent
    ) : Content()

    data class StatusComposer(
        val configuration: AppScreen.StatusComposer,
        val component: ComposerComponent
    ) : Content()
}
