package fr.outadoc.woolly.ui.feature.auth

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import fr.outadoc.woolly.common.feature.auth.AuthScreen
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.ui.navigation.rememberRouter
import kotlinx.coroutines.launch
import org.kodein.di.compose.instance

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AuthRouter() {
    val router = rememberRouter<AuthScreen>(
        initialConfiguration = { AuthScreen.AuthDomainSelect },
        handleBackButton = true
    )

    val scope = rememberCoroutineScope()
    val authenticationStateConsumer by instance<AuthenticationStateConsumer>()

    Scaffold(
        topBar = {
            Children(routerState = router.state) {
                TopAppBar(
                    title = { Text("Welcome to Woolly") },
                    navigationIcon = when {
                        router.state.value.backStack.isNotEmpty() -> {
                            @Composable {
                                IconButton(onClick = router::pop) {
                                    Icon(Icons.Default.ArrowBack, "Go back")
                                }
                            }
                        }
                        else -> null
                    }
                )
            }
        }
    ) { insets ->
        Children(
            routerState = router.state,
            animation = slide()
        ) { child ->
            when (val currentScreen = child.configuration) {
                AuthScreen.AuthDomainSelect -> {
                    DomainSelectScreen(
                        insets = insets,
                        onDomainSelected = { domain ->
                            router.push(
                                AuthScreen.AuthCodeInput(
                                    domain = domain
                                )
                            )
                        }
                    )
                }
                is AuthScreen.AuthCodeInput -> {
                    CodeInputScreen(
                        insets = insets,
                        domain = currentScreen.domain,
                        onSuccessfulAuthentication = { credentials ->
                            scope.launch {
                                authenticationStateConsumer.appendCredentials(credentials)
                            }
                        }
                    )
                }
            }
        }
    }
}
