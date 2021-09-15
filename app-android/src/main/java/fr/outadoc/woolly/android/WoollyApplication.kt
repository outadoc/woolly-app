package fr.outadoc.woolly.android

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import fr.outadoc.woolly.android.inject.AndroidAppDI
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.preference.PreferenceRepositoryImpl
import org.kodein.di.*

@Suppress("unused")
class WoollyApplication : Application(), DIAware {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override val di by subDI(AndroidAppDI, copy = Copy.All) {
        bindSingleton<PreferenceRepository> {
            PreferenceRepositoryImpl(
                appScope = instance(),
                prefs = applicationContext.dataStore
            )
        }
    }
}
