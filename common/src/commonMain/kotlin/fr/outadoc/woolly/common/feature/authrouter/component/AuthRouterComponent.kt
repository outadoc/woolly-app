package fr.outadoc.woolly.common.feature.authrouter.component

import com.arkivanov.decompose.*
import com.arkivanov.decompose.lifecycle.doOnDestroy
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.auth.state.UserCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kodein.di.DirectDI
import org.kodein.di.DirectDIAware
import org.kodein.di.factory
import org.kodein.di.instance

class AuthRouterComponent(
    componentContext: ComponentContext,
    override val directDI: DirectDI
) : ComponentContext by componentContext, DirectDIAware {

    private val componentScope = MainScope()

    private val router = router<AuthScreen, AuthContent>(
        initialConfiguration = { AuthScreen.AuthDomainSelect },
        handleBackButton = true,
        childFactory = ::createChild
    )

    val routerState: Value<RouterState<AuthScreen, AuthContent>> = router.state

    val shouldDisplayBackButton: Value<Boolean>
        get() = routerState.map { it.backStack.isNotEmpty() }

    private fun createChild(
        configuration: AuthScreen,
        componentContext: ComponentContext
    ): AuthContent = when (configuration) {
        is AuthScreen.AuthDomainSelect -> AuthContent.DomainSelect(
            configuration = configuration,
            component = createComponent(componentContext)
        )
        is AuthScreen.AuthCodeInput -> AuthContent.CodeInput(
            configuration = configuration,
            component = createComponent(componentContext)
        )
    }

    fun onBackPressed() {
        router.pop()
    }

    fun onDomainSelected(domain: String) {
        router.push(
            AuthScreen.AuthCodeInput(
                domain = domain
            )
        )
    }

    fun onSuccessfulAuthentication(credentials: UserCredentials) {
        componentScope.launch(Dispatchers.IO) {
            instance<AuthenticationStateConsumer>().appendCredentials(credentials)
        }
    }

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }

    private inline fun <reified T : Any> createComponent(componentContext: ComponentContext): T {
        return directDI.factory<ComponentContext, T>()(componentContext)
    }
}