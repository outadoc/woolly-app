package fr.outadoc.woolly.common.feature.timeline

class PublicTimelineScreenResources {

    fun getScreenTitle(subScreen: PublicTimelineSubScreen) = when (subScreen) {
        PublicTimelineSubScreen.Global -> "Global"
        PublicTimelineSubScreen.Local -> "Local"
    }
}
