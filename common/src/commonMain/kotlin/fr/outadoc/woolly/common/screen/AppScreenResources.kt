package fr.outadoc.woolly.common.screen

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen) = when (screen) {
        AppScreen.LocalTimeline -> "Local Timeline"
        AppScreen.GlobalTimeline -> "Public Timeline"
    }
}
