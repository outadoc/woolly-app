package fr.outadoc.woolly.android

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.defaultComponentContext
import fr.outadoc.woolly.common.feature.authrouter.component.AuthRouterComponent
import fr.outadoc.woolly.common.feature.mainrouter.component.MainRouterComponent
import fr.outadoc.woolly.common.feature.theme.AndroidSystemThemeDetector
import fr.outadoc.woolly.ui.WoollyApp
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.direct
import org.kodein.di.instance

class MainActivity : ComponentActivity() {

    private val di by closestDI()

    private var isReady = false
    private val insetsFlow = MutableStateFlow(Insets.NONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notifyConfiguration(resources.configuration)
        WindowCompat.setDecorFitsSystemWindows(window, false)

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

        ViewCompat.setOnApplyWindowInsetsListener(content) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            insetsFlow.tryEmit(insets)
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        notifyConfiguration(newConfig)
    }

    private fun notifyConfiguration(config: Configuration) {
        val systemThemeDetector by di.instance<AndroidSystemThemeDetector>()
        systemThemeDetector.notifyConfigurationChanged(config)
    }

    @Composable
    fun AndroidApp(
        mainRouterComponent: MainRouterComponent,
        authRouterComponent: AuthRouterComponent
    ) = withDI {
        val insetsPx by insetsFlow.collectAsState()
        val insetsDp = with(LocalDensity.current) {
            PaddingValues(
                start = insetsPx.left.toDp(),
                end = insetsPx.right.toDp(),
                top = insetsPx.top.toDp(),
                bottom = insetsPx.bottom.toDp()
            )
        }

        CompositionLocalProvider(
            LocalUriHandler provides CustomTabUriHandler(LocalContext.current),
        ) {
            WoollyApp(
                mainRouterComponent = mainRouterComponent,
                authRouterComponent = authRouterComponent,
                onFinishedLoading = { isReady = true },
                systemInsets = insetsDp
            )
        }
    }
}
