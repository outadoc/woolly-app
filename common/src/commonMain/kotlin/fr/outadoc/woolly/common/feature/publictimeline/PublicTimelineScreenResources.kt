package fr.outadoc.woolly.common.feature.publictimeline

class PublicTimelineScreenResources {

    fun getScreenTitle(subScreen: PublicTimelineSubScreen) = when (subScreen) {
        PublicTimelineSubScreen.Global -> "Federated"
        PublicTimelineSubScreen.Local -> "Local"
    }
}
