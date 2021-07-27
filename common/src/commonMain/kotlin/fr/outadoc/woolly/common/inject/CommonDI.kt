package fr.outadoc.woolly.common.inject

import androidx.paging.PagingConfig
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.account.AccountRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateRepository
import fr.outadoc.woolly.common.feature.auth.viewmodel.AuthViewModel
import fr.outadoc.woolly.common.feature.bookmarks.viewmodel.BookmarksViewModel
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.MastodonClientProviderImpl
import fr.outadoc.woolly.common.feature.favourites.viewmodel.FavouritesViewModel
import fr.outadoc.woolly.common.feature.home.viewmodel.HomeTimelineViewModel
import fr.outadoc.woolly.common.feature.notifications.viewmodel.NotificationsViewModel
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.publictimeline.viewmodel.PublicTimelineViewModel
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.search.viewmodel.SearchViewModel
import fr.outadoc.woolly.common.feature.tags.viewmodel.TrendingViewModel
import fr.outadoc.woolly.common.screen.AppScreenResources
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val CommonDI = DI {

    bindSingleton {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
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
    bindSingleton { FavouritesViewModel(instance(), instance(), instance()) }
}
