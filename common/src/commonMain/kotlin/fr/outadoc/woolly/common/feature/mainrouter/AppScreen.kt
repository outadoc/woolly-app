package fr.outadoc.woolly.common.feature.mainrouter

import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import fr.outadoc.woolly.common.feature.media.ImageAttachment
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineSubScreen
import fr.outadoc.woolly.common.feature.search.SearchSubScreen

sealed class AppScreen : Parcelable {

    @Parcelize
    object Account : AppScreen()

    @Parcelize
    object Bookmarks : AppScreen()

    @Parcelize
    object Favourites : AppScreen()

    @Parcelize
    object HomeTimeline : AppScreen()

    @Parcelize
    object Notifications : AppScreen()

    @Parcelize
    data class PublicTimeline(
        val subScreen: PublicTimelineSubScreen = PublicTimelineSubScreen.Local
    ) : AppScreen()

    @Parcelize
    data class Search(
        val subScreen: SearchSubScreen = SearchSubScreen.Statuses
    ) : AppScreen()

    @Parcelize
    data class StatusDetails(val statusId: String) : AppScreen()

    @Parcelize
    data class ImageViewer(val image: ImageAttachment) : AppScreen()

    @Parcelize
    object StatusComposer : AppScreen()
}