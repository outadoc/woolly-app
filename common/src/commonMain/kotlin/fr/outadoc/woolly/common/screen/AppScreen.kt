package fr.outadoc.woolly.common.screen

sealed class AppScreen {
    object HomeTimeline : AppScreen()
    object PublicTimeline : AppScreen()
    object Search : AppScreen()
    object Account : AppScreen()
}
