package fr.outadoc.woolly.common.feature.publictimeline

sealed class PublicTimelineSubScreen {
    object Global : PublicTimelineSubScreen()
    object Local : PublicTimelineSubScreen()
}
