package fr.outadoc.woolly.common.feature.search

sealed class SearchSubScreen {
    object Statuses : SearchSubScreen()
    object Accounts : SearchSubScreen()
    object Hashtags : SearchSubScreen()
}
