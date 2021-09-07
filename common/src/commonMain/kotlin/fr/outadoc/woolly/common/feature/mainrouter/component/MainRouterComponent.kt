package fr.outadoc.woolly.common.feature.mainrouter.component

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.media.toAppImage
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DirectDI
import org.kodein.di.factory

class MainRouterComponent(
    componentContext: ComponentContext,
    private val directDI: DirectDI
) : ComponentContext by componentContext {

    private val router = router<AppScreen, MainContent>(
        initialConfiguration = { AppScreen.HomeTimeline },
        handleBackButton = true,
        childFactory = ::createChild
    )

    val routerState: Value<RouterState<AppScreen, MainContent>> = router.state

    sealed class Event {
        data class OpenUri(val uri: String) : Event()
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    val shouldDisplayBackButton: Value<Boolean>
        get() = routerState.map { it.backStack.isNotEmpty() }

    val shouldDisplayComposeButton: Value<Boolean>
        get() = routerState.map { it.backStack.isEmpty() }

    fun onAttachmentClick(attachment: Attachment) {
        when (attachment) {
            is Attachment.Image -> router.push(
                AppScreen.ImageViewer(
                    image = attachment.toAppImage()
                )
            )
            else -> _events.tryEmit(Event.OpenUri(attachment.url))
        }
    }

    fun onStatusClick(status: Status) {
        router.push(
            AppScreen.StatusDetails(statusId = status.statusId)
        )
    }

    fun onStatusReplyClick(status: Status) {
        router.push(
            AppScreen.StatusComposer(
                inReplyToStatusId = status.statusId
            )
        )
    }

    fun onBackPressed() {
        router.pop()
    }

    fun onComposeStatusClicked() {
        router.push(AppScreen.StatusComposer())
    }

    fun onComposerDismissed() {
        router.pop()
    }

    suspend fun onScreenSelected(target: AppScreen) {
        scrollToTop()
        router.replaceCurrent(target)
    }

    suspend fun scrollToTop() {
        val activeContent = routerState.value.activeChild.instance
        (activeContent.component as? ScrollableComponent)
            ?.scrollToTop(activeContent.configuration)
    }

    private fun createChild(
        configuration: AppScreen,
        componentContext: ComponentContext
    ): MainContent = when (configuration) {
        is AppScreen.HomeTimeline -> MainContent.HomeTimeline(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.PublicTimeline -> MainContent.PublicTimeline(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.Notifications -> MainContent.Notifications(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.Search -> MainContent.Search(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.Account -> MainContent.Account(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.Bookmarks -> MainContent.Bookmarks(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.Favourites -> MainContent.Favourites(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.StatusDetails -> MainContent.StatusDetails(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.ImageViewer -> MainContent.ImageViewer(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.StatusComposer -> MainContent.StatusComposer(
            configuration = configuration,
            component = createComponent(componentContext)
        )
    }

    private inline fun <reified T : Any> createComponent(componentContext: ComponentContext): T {
        return directDI.factory<ComponentContext, T>()(componentContext)
    }
}
