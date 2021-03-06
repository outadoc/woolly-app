package fr.outadoc.woolly.common.feature.mainrouter

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import fr.outadoc.woolly.common.feature.composer.InReplyToStatusPayload
import fr.outadoc.woolly.common.feature.media.ImageAttachment

sealed class AppScreen : Parcelable {

    @Parcelize
    object MyAccount : AppScreen()

    @Parcelize
    object Bookmarks : AppScreen()

    @Parcelize
    object Favourites : AppScreen()

    @Parcelize
    object HomeTimeline : AppScreen()

    @Parcelize
    object Notifications : AppScreen()

    @Parcelize
    object PublicTimeline : AppScreen()

    @Parcelize
    object Search : AppScreen()

    @Parcelize
    data class StatusDetails(val statusId: String) : AppScreen()

    @Parcelize
    data class ImageViewer(val image: ImageAttachment) : AppScreen()

    @Parcelize
    data class StatusComposer(
        val inReplyToStatusPayload: InReplyToStatusPayload? = null
    ) : AppScreen()

    @Parcelize
    data class AccountDetails(val accountId: String) : AppScreen()

    @Parcelize
    data class HashtagTimeline(val hashtag: String) : AppScreen()
}
