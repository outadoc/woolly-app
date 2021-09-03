package fr.outadoc.woolly.common.feature.mainrouter.component

import com.arkivanov.decompose.ComponentContext
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

    abstract val configuration: AppScreen
    abstract val component: ComponentContext

    data class Account(
        override val configuration: AppScreen.Account,
        override val component: AccountComponent
    ) : Content()

    data class Bookmarks(
        override val configuration: AppScreen.Bookmarks,
        override val component: BookmarksComponent
    ) : Content()

    data class Favourites(
        override val configuration: AppScreen.Favourites,
        override val component: FavouritesComponent
    ) : Content()

    data class HomeTimeline(
        override val configuration: AppScreen.HomeTimeline,
        override val component: HomeTimelineComponent
    ) : Content()

    data class Notifications(
        override val configuration: AppScreen.Notifications,
        override val component: NotificationsComponent
    ) : Content()

    data class PublicTimeline(
        override val configuration: AppScreen.PublicTimeline,
        override val component: PublicTimelineComponent
    ) : Content()

    data class Search(
        override val configuration: AppScreen.Search,
        override val component: SearchComponent
    ) : Content()

    data class StatusDetails(
        override val configuration: AppScreen.StatusDetails,
        override val component: StatusDetailsComponent
    ) : Content()

    data class ImageViewer(
        override val configuration: AppScreen.ImageViewer,
        override val component: AttachmentViewerComponent
    ) : Content()

    data class StatusComposer(
        override val configuration: AppScreen.StatusComposer,
        override val component: ComposerComponent
    ) : Content()
}
