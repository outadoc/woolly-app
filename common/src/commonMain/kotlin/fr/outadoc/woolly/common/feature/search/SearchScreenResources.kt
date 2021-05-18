package fr.outadoc.woolly.common.feature.search

class SearchScreenResources {

    fun getScreenTitle(subScreen: SearchSubScreen) = when (subScreen) {
        SearchSubScreen.Statuses -> "Toots"
        SearchSubScreen.Accounts -> "Accounts"
        SearchSubScreen.Hashtags -> "Hashtags"
    }
}
