package fr.outadoc.woolly.desktop.inject

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import fr.outadoc.woolly.common.feature.preference.PreferenceFileProvider
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.preference.PreferenceRepositoryImpl
import fr.outadoc.woolly.common.inject.CommonDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.Copy
import org.kodein.di.bindSingleton
import org.kodein.di.subDI

val DesktopAppDI = subDI(CommonDI, copy = Copy.All) {
    bindSingleton {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    bindSingleton<PreferenceRepository> {
        PreferenceRepositoryImpl(
            prefs = PreferenceDataStoreFactory.create {
                PreferenceFileProvider.preferenceFile!!
            }
        )
    }
}
