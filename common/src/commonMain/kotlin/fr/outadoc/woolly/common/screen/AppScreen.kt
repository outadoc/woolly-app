package fr.outadoc.woolly.common.screen

sealed class AppScreen {
    object GlobalTimeline : AppScreen()
    object LocalTimeline : AppScreen()
    object Search : AppScreen()
}
