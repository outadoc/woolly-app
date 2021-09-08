package fr.outadoc.woolly.common.feature.status

data class StatusDelta(
    val isFavourited: Boolean? = null,
    val isBoosted: Boolean? = null,
    val isBookmarked: Boolean? = null
)
