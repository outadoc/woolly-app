package fr.outadoc.woolly.android

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.defaultComponentContext
import fr.outadoc.woolly.common.feature.authrouter.component.AuthRouterComponent
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.ui.WoollyApp
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.direct

class MainActivity : ComponentActivity() {

    private val di by closestDI()

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val componentContext = defaultComponentContext()

        val mainRouterComponent = MainRouterComponent(
            componentContext = componentContext.childContext("main"),
            directDI = di.direct
        )

        val authRouterComponent = AuthRouterComponent(
            componentContext = componentContext.childContext("auth"),
            directDI = di.direct
        )

        setContent {
            AndroidApp(
                mainRouterComponent = mainRouterComponent,
                authRouterComponent = authRouterComponent
            )
        }

        // Disable drawing until everything is loaded.
        // The splash screen will be visible until then.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    @Composable
    fun AndroidApp(
        mainRouterComponent: MainRouterComponent,
        authRouterComponent: AuthRouterComponent
    ) = withDI {
        CompositionLocalProvider(
            LocalUriHandler provides CustomTabUriHandler(LocalContext.current),
        ) {
            WoollyApp(
                mainRouterComponent = mainRouterComponent,
                authRouterComponent = authRouterComponent,
                onFinishedLoading = { isReady = true }
            )
        }
    }
}
