package fr.outadoc.woolly.common.inject

import androidx.paging.PagingConfig
import com.arkivanov.decompose.ComponentContext
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.account.AccountRepositoryImpl
import fr.outadoc.woolly.common.feature.account.component.AccountDetailsComponent
import fr.outadoc.woolly.common.feature.account.component.MyAccountComponent
import fr.outadoc.woolly.common.feature.auth.component.CodeInputComponent
import fr.outadoc.woolly.common.feature.auth.component.DomainSelectComponent
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepository
import fr.outadoc.woolly.common.feature.auth.proxy.AuthProxyRepositoryImpl
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateConsumer
import fr.outadoc.woolly.common.feature.auth.state.AuthenticationStateRepository
import fr.outadoc.woolly.common.feature.bookmarks.component.BookmarksComponent
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.MastodonClientProviderImpl
import fr.outadoc.woolly.common.feature.composer.SimpleThreadedStatusPoster
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.feature.composer.component.ComposerComponent
import fr.outadoc.woolly.common.feature.favourites.component.FavouritesComponent
import fr.outadoc.woolly.common.feature.home.component.HomeTimelineComponent
import fr.outadoc.woolly.common.feature.media.component.AttachmentViewerComponent
import fr.outadoc.woolly.common.feature.notifications.component.NotificationsComponent
import fr.outadoc.woolly.common.feature.publictimeline.PublicTimelineScreenResources
import fr.outadoc.woolly.common.feature.publictimeline.component.PublicTimelineComponent
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.search.component.SearchComponent
import fr.outadoc.woolly.common.feature.status.StatusActionRepository
import fr.outadoc.woolly.common.feature.status.StatusPagingRepository
import fr.outadoc.woolly.common.feature.statusdetails.component.StatusDetailsComponent
import fr.outadoc.woolly.common.feature.tags.component.HashtagTimelineComponent
import fr.outadoc.woolly.common.feature.theme.ThemeProvider
import fr.outadoc.woolly.common.feature.time.TimeRepository
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.kodein.di.DI
import org.kodein.di.bindFactory
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
    bindSingleton { StatusPagingRepository(instance(), instance(), instance()) }

    bindSingleton<MastodonClientProvider> { MastodonClientProviderImpl(instance(), instance()) }
    bindSingleton<AccountRepository> { AccountRepositoryImpl(instance(), instance()) }
    bindSingleton { TimeRepository() }

    bindSingleton { ThemeProvider(instance(), instance(), instance()) }

    // TODO WorkManager-based impl for Android
    bindSingleton<StatusPoster> { SimpleThreadedStatusPoster(instance(), instance()) }

    // Inject components
    bindFactory { componentContext: ComponentContext ->
        HomeTimelineComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        PublicTimelineComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        NotificationsComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        SearchComponent(componentContext, instance(), instance(), instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        MyAccountComponent(componentContext, instance(), instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        BookmarksComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        FavouritesComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        StatusDetailsComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        AttachmentViewerComponent(componentContext)
    }

    bindFactory { componentContext: ComponentContext ->
        ComposerComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        AccountDetailsComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        HashtagTimelineComponent(componentContext, instance(), instance())
    }

    // Auth components
    bindFactory { componentContext: ComponentContext ->
        CodeInputComponent(componentContext, instance(), instance())
    }

    bindFactory { componentContext: ComponentContext ->
        DomainSelectComponent(componentContext, instance())
    }
}
