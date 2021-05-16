import androidx.compose.desktop.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.IntSize
import fr.outadoc.woolly.common.App
import fr.outadoc.woolly.common.feature.preference.DesktopPreferenceRepositoryImpl
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI
import org.kodein.di.instance

fun main() = Window(
    title = "Woolly",
    size = IntSize(width = 750, height = 750)
) {
    DesktopApp()
}

@Composable
private fun DesktopApp() = withDI(di) {
    CompositionLocalProvider(LocalUriHandler provides DesktopUriHandler()) {
        App()
    }
}

private val di = DI {

    bindSingleton { Json {} }
    bindSingleton<CoroutineScope> { GlobalScope }

    bindSingleton<PreferenceRepository> {
        DesktopPreferenceRepositoryImpl(instance(), instance())
    }
}
