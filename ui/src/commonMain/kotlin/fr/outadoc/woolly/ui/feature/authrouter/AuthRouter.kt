package fr.outadoc.woolly.ui.feature.authrouter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import fr.outadoc.woolly.common.feature.authrouter.component.AuthContent
import fr.outadoc.woolly.common.feature.authrouter.component.AuthRouterComponent
import fr.outadoc.woolly.ui.common.PaddedTopAppBar
import fr.outadoc.woolly.ui.common.takeTop
import fr.outadoc.woolly.ui.feature.auth.CodeInputScreen
import fr.outadoc.woolly.ui.feature.auth.DomainSelectScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AuthRouter(
    component: AuthRouterComponent,
    systemInsets: PaddingValues = PaddingValues()
) {
    Scaffold(
        topBar = {
            Children(routerState = component.routerState) {
                PaddedTopAppBar(
                    contentPadding = systemInsets.takeTop(),
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
                        modifier = Modifier.padding(insets),
                        component = content.component,
                        onDomainSelected = component::onDomainSelected
                    )
                }
                is AuthContent.CodeInput -> {
                    CodeInputScreen(
                        modifier = Modifier.padding(insets),
                        component = content.component,
                        domain = content.configuration.domain,
                        onSuccessfulAuthentication = component::onSuccessfulAuthentication
                    )
                }
            }
        }
    }
}
