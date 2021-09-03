package fr.outadoc.woolly.common.feature.mainrouter.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.account.component.AccountComponent
import fr.outadoc.woolly.common.feature.bookmarks.component.BookmarksComponent
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
import fr.outadoc.woolly.common.feature.favourites.component.FavouritesComponent
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.common.feature.media.component.AttachmentViewerComponent
import fr.outadoc.woolly.common.feature.media.toAppImage
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.common.screen.AppScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.factory

class MainRouterComponent(
    componentContext: ComponentContext,
    override val di: DI
) : ComponentContext by componentContext, DIAware {

    private val router = router<AppScreen, Content>(
        initialConfiguration = { AppScreen.HomeTimeline },
        handleBackButton = true,
        childFactory = ::createChild
    )

    val routerState: Value<RouterState<AppScreen, Content>> = router.state

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

    fun onBackPressed() {
        router.pop()
    }

    fun onComposeStatusClicked() {
        router.push(AppScreen.StatusComposer)
    }

    fun onComposerDismissed() {
        router.pop()
    }

    private fun createChild(config: AppScreen, componentContext: ComponentContext): Content =
        when (config) {
            is AppScreen.HomeTimeline -> {
                val component by factory<ComponentContext, HomeTimelineComponent>()
                Content.HomeTimeline(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.PublicTimeline -> {
                val component by factory<ComponentContext, PublicTimelineComponent>()
                Content.PublicTimeline(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Notifications -> {
                val component by factory<ComponentContext, NotificationsComponent>()
                Content.Notifications(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Search -> {
                val component by factory<ComponentContext, SearchComponent>()
                Content.Search(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Account -> {
                val component by factory<ComponentContext, AccountComponent>()
                Content.Account(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Bookmarks -> {
                val component by factory<ComponentContext, BookmarksComponent>()
                Content.Bookmarks(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Favourites -> {
                val component by factory<ComponentContext, FavouritesComponent>()
                Content.Favourites(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.StatusDetails -> {
                val component by factory<ComponentContext, StatusDetailsComponent>()
                Content.StatusDetails(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.ImageViewer -> {
                val component by factory<ComponentContext, AttachmentViewerComponent>()
                Content.ImageViewer(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.StatusComposer -> {
                val component by factory<ComponentContext, ComposerComponent>()
                Content.StatusComposer(
                    configuration = config,
                    component = component(componentContext)
                )
            }
        }
}

