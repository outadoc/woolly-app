package fr.outadoc.woolly.android

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import fr.outadoc.woolly.ui.WoollyApp
import fr.outadoc.woolly.ui.navigation.LocalBackPressedDispatcher
import fr.outadoc.woolly.ui.navigation.main.MainRouterComponent
import org.kodein.di.compose.LocalDI
import org.kodein.di.compose.withDI

class MainActivity : ComponentActivity() {

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AndroidApp() }

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
    fun AndroidApp() = withDI {
        val di = LocalDI.current
        val mainRouterComponent = remember {
            MainRouterComponent(
                componentContext = DefaultComponentContext(lifecycle),
                di = di
            )
        }

        CompositionLocalProvider(
            LocalUriHandler provides CustomTabUriHandler(LocalContext.current),
            LocalBackPressedDispatcher provides BackPressedDispatcher(onBackPressedDispatcher)
        ) {
            WoollyApp(
                mainRouterComponent = mainRouterComponent,
                onFinishedLoading = { isReady = true }
            )
        }
    }
}
