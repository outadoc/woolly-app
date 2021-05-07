package fr.outadoc.mastodonk.common.screen

sealed class AppScreen {
    object GlobalTimeline : AppScreen()
    object LocalTimeline : AppScreen()
}
