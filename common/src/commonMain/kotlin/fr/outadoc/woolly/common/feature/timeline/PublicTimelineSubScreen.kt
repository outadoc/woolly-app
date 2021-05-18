package fr.outadoc.woolly.common.feature.timeline

sealed class PublicTimelineSubScreen {
    object Global : PublicTimelineSubScreen()
    object Local : PublicTimelineSubScreen()
}
