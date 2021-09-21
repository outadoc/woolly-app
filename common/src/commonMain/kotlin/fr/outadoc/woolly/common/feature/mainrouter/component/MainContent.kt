package fr.outadoc.woolly.common.feature.mainrouter.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent
import fr.outadoc.woolly.common.feature.account.component.MyAccountComponent
import fr.outadoc.woolly.common.feature.bookmarks.component.BookmarksComponent
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
import fr.outadoc.woolly.common.feature.favourites.component.FavouritesComponent
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.media.component.AttachmentViewerComponent
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.common.feature.tags.component.HashtagTimelineComponent

sealed class MainContent {

    abstract val configuration: AppScreen
    abstract val component: ComponentContext

    data class MyAccount(
        override val configuration: AppScreen.MyAccount,
        override val component: MyAccountComponent
    ) : MainContent()

    data class Bookmarks(
        override val configuration: AppScreen.Bookmarks,
        override val component: BookmarksComponent
    ) : MainContent()

    data class Favourites(
        override val configuration: AppScreen.Favourites,
        override val component: FavouritesComponent
    ) : MainContent()

    data class HomeTimeline(
        override val configuration: AppScreen.HomeTimeline,
        override val component: HomeTimelineComponent
    ) : MainContent()

    data class Notifications(
        override val configuration: AppScreen.Notifications,
        override val component: NotificationsComponent
    ) : MainContent()

    data class PublicTimeline(
        override val configuration: AppScreen.PublicTimeline,
        override val component: PublicTimelineComponent
    ) : MainContent()

    data class Search(
        override val configuration: AppScreen.Search,
        override val component: SearchComponent
    ) : MainContent()

    data class StatusDetails(
        override val configuration: AppScreen.StatusDetails,
        override val component: StatusDetailsComponent
    ) : MainContent()

    data class ImageViewer(
        override val configuration: AppScreen.ImageViewer,
        override val component: AttachmentViewerComponent
    ) : MainContent()

    data class StatusComposer(
        override val configuration: AppScreen.StatusComposer,
        override val component: ComposerComponent
    ) : MainContent()

    data class AccountDetails(
        override val configuration: AppScreen.AccountDetails,
        override val component: AccountDetailsComponent
    ) : MainContent()

    data class HashtagTimeline(
        override val configuration: AppScreen.HashtagTimeline,
        override val component: HashtagTimelineComponent
    ) : MainContent()
}
