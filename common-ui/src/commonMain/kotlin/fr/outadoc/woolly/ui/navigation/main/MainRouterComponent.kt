package fr.outadoc.woolly.ui.navigation.main

import androidx.compose.material.*
import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
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
import org.kodein.di.DI
import org.kodein.di.factory

class MainRouterComponent(
    componentContext: ComponentContext,
    private val di: DI
) : ComponentContext by componentContext {

    private val router = router<AppScreen, Content>(
        initialConfiguration = { AppScreen.HomeTimeline },
        handleBackButton = true,
        childFactory = ::createChild
    )

    val routerState: Value<RouterState<AppScreen, Content>> = router.state

    private fun createChild(config: AppScreen, componentContext: ComponentContext): Content =
        when (config) {
            is AppScreen.HomeTimeline -> {
                val component by di.factory<ComponentContext, HomeTimelineComponent>()
                Content.HomeTimeline(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.PublicTimeline -> {
                val component by di.factory<ComponentContext, PublicTimelineComponent>()
                Content.PublicTimeline(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Notifications -> {
                val component by di.factory<ComponentContext, NotificationsComponent>()
                Content.Notifications(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Search -> {
                val component by di.factory<ComponentContext, SearchComponent>()
                Content.Search(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Account -> {
                val component by di.factory<ComponentContext, AccountComponent>()
                Content.Account(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Bookmarks -> {
                val component by di.factory<ComponentContext, BookmarksComponent>()
                Content.Bookmarks(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.Favourites -> {
                val component by di.factory<ComponentContext, FavouritesComponent>()
                Content.Favourites(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.StatusDetails -> {
                val component by di.factory<ComponentContext, StatusDetailsComponent>()
                Content.StatusDetails(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.ImageViewer -> {
                val component by di.factory<ComponentContext, AttachmentViewerComponent>()
                Content.ImageViewer(
                    configuration = config,
                    component = component(componentContext)
                )
            }

            is AppScreen.StatusComposer -> {
                val component by di.factory<ComponentContext, ComposerComponent>()
                Content.StatusComposer(
                    configuration = config,
                    component = component(componentContext)
                )
            }
        }

    val isBackStackEmpty: Boolean
        get() = routerState.value.backStack.isEmpty()

    fun onAttachmentClick(attachment: Attachment) {
        when (attachment) {
            is Attachment.Image -> router.push(
                AppScreen.ImageViewer(
                    image = attachment.toAppImage()
                )
            )
            else -> uriHandler.openUri(attachment.url)
        }
    }

    fun onStatusClick(status: Status) {
        router.push(
            AppScreen.StatusDetails(statusId = status.statusId)
        )
    }
}

