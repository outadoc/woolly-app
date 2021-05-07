package fr.outadoc.mastodonk.common

class AppScreenResources {

    fun getScreenTitle(screen: AppScreen): String {
        return when (screen) {
            AppScreen.LocalTimeline -> "Local Timeline"
            AppScreen.PublicTimeline -> "Public Timeline"
        }
    }
}
