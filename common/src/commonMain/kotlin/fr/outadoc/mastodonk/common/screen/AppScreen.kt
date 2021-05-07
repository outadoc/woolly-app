package fr.outadoc.mastodonk.common.screen

sealed class AppScreen {
    object PublicTimeline : AppScreen()
    object LocalTimeline : AppScreen()
}
