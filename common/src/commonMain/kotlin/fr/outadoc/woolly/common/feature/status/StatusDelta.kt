package fr.outadoc.woolly.common.feature.status

import fr.outadoc.mastodonk.api.entity.Status

data class StatusDelta(
    val isFavourited: Boolean? = null,
    val isBoosted: Boolean? = null,
    val isBookmarked: Boolean? = null
)

operator fun Status.plus(statusDelta: StatusDelta?): Status =
    if (statusDelta == null) this
    else copy(
        isBoosted = statusDelta.isBoosted ?: isBoosted,
        isFavourited = statusDelta.isFavourited ?: isFavourited,
        isBookmarked = statusDelta.isBookmarked ?: isBookmarked
    )
