package fr.outadoc.woolly.common.feature.search

sealed class SubSearchScreen {
    object Statuses : SubSearchScreen()
    object Accounts : SubSearchScreen()
    object Hashtags : SubSearchScreen()
}
