package fr.outadoc.woolly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import fr.outadoc.woolly.common.WoollyApp
import fr.outadoc.woolly.common.navigation.LocalBackPressedDispatcher
import org.kodein.di.compose.withDI

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AndroidApp() }
    }

    @Composable
    fun AndroidApp() = withDI {
        CompositionLocalProvider(
            LocalUriHandler provides CustomTabUriHandler(LocalContext.current),
            LocalBackPressedDispatcher provides BackPressedDispatcher(onBackPressedDispatcher)
        ) {
            WoollyApp()
        }
    }
}
