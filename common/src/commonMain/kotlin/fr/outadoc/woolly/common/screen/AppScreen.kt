package fr.outadoc.woolly.common.screen

sealed class AppScreen {
    object HomeTimeline : AppScreen()
    object GlobalTimeline : AppScreen()
    object LocalTimeline : AppScreen()
    object Search : AppScreen()
}
