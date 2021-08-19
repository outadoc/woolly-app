package fr.outadoc.woolly.common.feature.status

import fr.outadoc.mastodonk.api.entity.Status

sealed class StatusAction {
    data class Favourite(val status: Status) : StatusAction()
    data class UndoFavourite(val status: Status) : StatusAction()
    data class Boost(val status: Status) : StatusAction()
    data class UndoBoost(val status: Status) : StatusAction()
    data class Bookmark(val status: Status) : StatusAction()
    data class UndoBookmark(val status: Status) : StatusAction()
}
