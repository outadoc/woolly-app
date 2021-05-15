package fr.outadoc.woolly.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.search.SearchScreenResources
import fr.outadoc.woolly.common.feature.search.repository.SearchRepository
import fr.outadoc.woolly.common.feature.timeline.repository.StatusRepository
import fr.outadoc.woolly.common.feature.timeline.usecase.AnnotateStatusUseCase
import fr.outadoc.woolly.common.screen.AppScreenResources
import fr.outadoc.woolly.common.ui.AppTheme
import fr.outadoc.woolly.common.ui.Router
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
        MastodonClient {
            domain = "mastodon.social"
        }
    }

    bindSingleton { kotlinx.serialization.json.Json {} }

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
    bindSingleton { AnnotateStatusUseCase(instance()) }

    bindSingleton { StatusRepository(instance()) }
    bindSingleton { SearchRepository(instance()) }
}

@Composable
fun App() = subDI(diBuilder = di) {
    var isDarkModeEnabled by remember { mutableStateOf(true) }
    AppTheme(isDarkModeEnabled = isDarkModeEnabled) {
        Router {
            isDarkModeEnabled = !isDarkModeEnabled
        }
    }
}
