package fr.outadoc.woolly.common.feature.status

import fr.outadoc.mastodonk.api.entity.Status

sealed class StatusAction {

    abstract val status: Status

    data class Favourite(override val status: Status) : StatusAction()
    data class UndoFavourite(override val status: Status) : StatusAction()
    data class Boost(override val status: Status) : StatusAction()
    data class UndoBoost(override val status: Status) : StatusAction()
    data class Bookmark(override val status: Status) : StatusAction()
    data class UndoBookmark(override val status: Status) : StatusAction()
}
