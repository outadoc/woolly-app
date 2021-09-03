package fr.outadoc.woolly.ui.feature.authrouter

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import fr.outadoc.woolly.common.feature.authrouter.component.AuthContent
import fr.outadoc.woolly.common.feature.authrouter.component.AuthRouterComponent
import fr.outadoc.woolly.ui.feature.auth.CodeInputScreen
import fr.outadoc.woolly.ui.feature.auth.DomainSelectScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AuthRouter(component: AuthRouterComponent) {
    Scaffold(
        topBar = {
            Children(routerState = component.routerState) {
                TopAppBar(
                    title = { Text("Welcome to Woolly") },
                    navigationIcon = if (component.shouldDisplayBackButton.value) {
                        @Composable {
                            IconButton(onClick = component::onBackPressed) {
                                Icon(Icons.Default.ArrowBack, "Go back")
                            }
                        }
                    } else null
                )
            }
        }
    ) { insets ->
        Children(
            routerState = component.routerState,
            animation = slide()
        ) { child ->
            when (val content = child.instance) {
                is AuthContent.DomainSelect -> {
                    DomainSelectScreen(
                        component = content.component,
                        insets = insets,
                        onDomainSelected = component::onDomainSelected
                    )
                }
                is AuthContent.CodeInput -> {
                    CodeInputScreen(
                        component = content.component,
                        insets = insets,
                        domain = content.configuration.domain,
                        onSuccessfulAuthentication = component::onSuccessfulAuthentication
                    )
                }
            }
        }
    }
}
