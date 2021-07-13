package fr.outadoc.woolly.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.PagingConfig
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.account.AccountRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateRepository
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateSupplier
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import fr.outadoc.woolly.common.feature.bookmarks.viewmodel.BookmarksViewModel
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.MastodonClientProviderImpl
import fr.outadoc.woolly.common.feature.home.viewmodel.HomeTimelineViewModel
import fr.outadoc.woolly.common.feature.notifications.viewmodel.NotificationsViewModel
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.publictimeline.viewmodel.PublicTimelineViewModel
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.search.viewmodel.SearchViewModel
import fr.outadoc.woolly.common.feature.tags.viewmodel.TrendingViewModel
import fr.outadoc.woolly.common.navigation.Router
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.ColorScheme
import fr.outadoc.woolly.common.ui.WoollyTheme
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.LocalDI
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

    bindSingleton {
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            maxSize = 200
        )
    }

    bindSingleton { AppScreenResources() }
    bindSingleton { SearchScreenResources() }
    bindSingleton { PublicTimelineScreenResources() }

    bindSingleton<AuthProxyRepository> { AuthProxyRepositoryImpl(instance()) }

    bindSingleton { AuthenticationStateRepository(instance()) }
    bindSingleton<AuthenticationStateSupplier> { instance<AuthenticationStateRepository>() }
    bindSingleton<AuthenticationStateConsumer> { instance<AuthenticationStateRepository>() }

    bindSingleton<MastodonClientProvider> { MastodonClientProviderImpl(instance(), instance()) }
    bindSingleton<AccountRepository> { AccountRepositoryImpl(instance(), instance()) }

    bindSingleton { AuthViewModel(instance(), instance(), instance()) }
    bindSingleton { HomeTimelineViewModel(instance(), instance(), instance()) }
    bindSingleton { PublicTimelineViewModel(instance(), instance(), instance()) }
    bindSingleton { SearchViewModel(instance(), instance(), instance()) }
    bindSingleton { TrendingViewModel(instance()) }
    bindSingleton { NotificationsViewModel(instance(), instance(), instance()) }
    bindSingleton { BookmarksViewModel(instance(), instance(), instance()) }
}

@Composable
fun App() = subDI(diBuilder = di) {
    val di = LocalDI.current
    val prefs by di.instance<PreferenceRepository>()

    var colorScheme by rememberSaveable { mutableStateOf(prefs.colorScheme) }

    WoollyTheme(isDarkMode = colorScheme == ColorScheme.Dark) {
        Router(
            colorScheme = colorScheme,
            onColorSchemeChanged = {
                prefs.colorScheme = it
                colorScheme = it
            }
        )
    }
}
