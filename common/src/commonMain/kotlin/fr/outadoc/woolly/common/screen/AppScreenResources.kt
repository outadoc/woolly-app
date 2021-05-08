package fr.outadoc.woolly.common.screen

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen): String {
        return when (screen) {
            AppScreen.LocalTimeline -> "Local Timeline"
            AppScreen.GlobalTimeline -> "Public Timeline"
        }
    }
}
