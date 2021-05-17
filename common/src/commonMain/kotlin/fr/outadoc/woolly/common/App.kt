package fr.outadoc.woolly.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoPublisher
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoRepository
import fr.outadoc.woolly.common.feature.auth.info.AuthInfoSubscriber
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.navigation.Router
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.AppTheme
import fr.outadoc.woolly.common.ui.ColorScheme
import fr.outadoc.woolly.htmltext.HtmlParser
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.subDI
import org.kodein.di.instance

private val di = fun DI.MainBuilder.() {

    bindSingleton {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json = instance())
            }
        }
    }

    bindSingleton { AppScreenResources() }
    bindSingleton { SearchScreenResources() }

    bindSingleton { HtmlParser() }

    bindSingleton<AuthProxyRepository> { AuthProxyRepositoryImpl(instance()) }

    bindSingleton { AuthInfoRepository(instance()) }
    bindSingleton<AuthInfoPublisher> { instance<AuthInfoRepository>() }
    bindSingleton<AuthInfoSubscriber> { instance<AuthInfoRepository>() }

    bindSingleton { AuthViewModel(instance(), instance(), instance()) }
}

@Composable
fun App() = subDI(diBuilder = di) {
    var colorScheme by remember { mutableStateOf(ColorScheme.Dark) }
    AppTheme(isDarkMode = colorScheme == ColorScheme.Dark) {
        Router(
            colorScheme = colorScheme,
            onColorSchemeChanged = { colorScheme = it }
        )
    }
}
