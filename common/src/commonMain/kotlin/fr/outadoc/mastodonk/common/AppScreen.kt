package fr.outadoc.mastodonk.common

sealed class AppScreen {
    object PublicTimeline : AppScreen()
    object LocalTimeline : AppScreen()
}
