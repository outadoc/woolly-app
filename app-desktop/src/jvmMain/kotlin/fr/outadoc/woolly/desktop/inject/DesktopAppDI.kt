package fr.outadoc.woolly.desktop.inject

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import fr.outadoc.woolly.common.feature.preference.PreferenceFileProvider
import fr.outadoc.woolly.common.feature.preference.PreferenceRepository
import fr.outadoc.woolly.common.feature.preference.PreferenceRepositoryImpl
import fr.outadoc.woolly.common.feature.theme.DesktopSystemThemeDetector
import fr.outadoc.woolly.common.feature.theme.SystemThemeDetector
import fr.outadoc.woolly.common.inject.CommonDI
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.Copy
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.subDI

val DesktopAppDI = subDI(CommonDI, copy = Copy.All) {

    bindSingleton { AppScreenResources() }

    bindSingleton {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    bindSingleton<PreferenceRepository> {
        PreferenceRepositoryImpl(
            appScope = instance(),
            prefs = PreferenceDataStoreFactory.create {
                PreferenceFileProvider.preferenceFile!!
            }
        )
    }

    bindSingleton<SystemThemeDetector> { DesktopSystemThemeDetector(instance()) }
}
