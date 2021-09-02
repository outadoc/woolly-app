package fr.outadoc.woolly.common.inject

import androidx.paging.PagingConfig
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.account.AccountRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateRepository
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.MastodonClientProviderImpl
import fr.outadoc.woolly.common.feature.composer.SimpleThreadedStatusPoster
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.time.TimeRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
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

    bindSingleton { SearchScreenResources() }
    bindSingleton { PublicTimelineScreenResources() }

    bindSingleton<AuthProxyRepository> { AuthProxyRepositoryImpl(instance()) }

    bindSingleton { AuthenticationStateRepository(instance()) }
    bindSingleton<AuthenticationStateConsumer> { instance<AuthenticationStateRepository>() }
    bindSingleton { StatusActionRepository(instance(), instance()) }

    bindSingleton<MastodonClientProvider> { MastodonClientProviderImpl(instance(), instance()) }
    bindSingleton<AccountRepository> { AccountRepositoryImpl(instance(), instance()) }
    bindSingleton { TimeRepository() }

    // TODO WorkManager-based impl for Android
    bindSingleton<StatusPoster> { SimpleThreadedStatusPoster(instance(), instance()) }
}
