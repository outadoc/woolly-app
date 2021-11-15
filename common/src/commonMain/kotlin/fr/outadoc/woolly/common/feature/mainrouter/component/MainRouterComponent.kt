package fr.outadoc.woolly.common.feature.mainrouter.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.composer.InReplyToStatusPayload
import fr.outadoc.woolly.common.feature.mainrouter.AppScreen
import fr.outadoc.woolly.common.feature.media.toAppImage
import fr.outadoc.woolly.common.feature.navigation.ScrollableComponent
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.kodein.di.DirectDI
import org.kodein.di.factory
import org.kodein.di.instance

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

    private val accountRepository: AccountRepository by lazy {
        directDI.instance()
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val componentScope = getScope()

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
            else -> componentScope.launch {
                _events.emit(Event.OpenUri(attachment.url))
            }
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
                inReplyToStatusPayload = InReplyToStatusPayload(
                    statusId = status.statusId,
                    acct = status.account.acct
                )
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

    fun onAccountClick(account: Account) {
        val target = when (accountRepository.currentAccount.value?.accountId) {
            account.accountId -> AppScreen.MyAccount
            else -> AppScreen.AccountDetails(
                accountId = account.accountId
            )
        }

        router.push(target)
    }

    fun onHashtagClick(hashtag: String) {
        router.push(
            AppScreen.HashtagTimeline(
                hashtag = hashtag
            )
        )
    }

    suspend fun onScreenSelected(target: AppScreen) {
        scrollToTop(target)

        // Pop the complete stack and add our target screen on top
        router.navigate { it.dropLastWhile { true } + target }
    }

    suspend fun scrollToTop(target: AppScreen? = null) {
        val activeContent = routerState.value.activeChild.instance
        val activeComponent = activeContent.component

        val shouldScrollUp = target == null || target == activeContent.configuration

        if (shouldScrollUp && activeComponent is ScrollableComponent) {
            activeComponent.scrollToTop(activeContent.configuration)
        }
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
        is AppScreen.MyAccount -> MainContent.MyAccount(
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
        is AppScreen.AccountDetails -> MainContent.AccountDetails(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AppScreen.HashtagTimeline -> MainContent.HashtagTimeline(
            configuration = configuration,
            component = createComponent(componentContext)
        )
    }

    private inline fun <reified T : Any> createComponent(componentContext: ComponentContext): T {
        return directDI.factory<ComponentContext, T>()(componentContext)
    }
}
