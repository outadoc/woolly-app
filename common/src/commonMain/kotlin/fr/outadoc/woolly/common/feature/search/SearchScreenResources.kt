package fr.outadoc.woolly.common.feature.search

class SearchScreenResources {

    fun getScreenTitle(screen: SubSearchScreen) = when (screen) {
        SubSearchScreen.Statuses -> "Toots"
        SubSearchScreen.Accounts -> "Accounts"
        SubSearchScreen.Hashtags -> "Hashtags"
    }
}