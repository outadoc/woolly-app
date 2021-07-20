package fr.outadoc.woolly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import fr.outadoc.woolly.common.App
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.preference.PreferenceRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.compose.withDI
import org.kodein.di.instance

class MainActivity : ComponentActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AndroidApp() }
    }

    @Composable
    fun AndroidApp() = withDI(di) {
        val context = LocalContext.current
        CompositionLocalProvider(
            LocalUriHandler provides CustomTabUriHandler(context)
        ) {
            App()
        }
    }

    private val di = DI {

        bindSingleton { Json {} }
        bindSingleton<CoroutineScope> { lifecycleScope }

        bindSingleton<PreferenceRepository> {
            PreferenceRepositoryImpl(prefs = dataStore, json = instance())
        }
    }
}
