package fr.outadoc.woolly.android.inject

import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import fr.outadoc.woolly.common.feature.theme.AndroidSystemThemeDetector
import fr.outadoc.woolly.common.feature.theme.SystemThemeDetector
import fr.outadoc.woolly.common.inject.CommonDI
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.*
import org.kodein.di.android.ActivityRetainedScope

val AndroidAppDI = subDI(CommonDI, copy = Copy.All) {

    bindSingleton { AppScreenResources() }

    bind<CoroutineScope>() with scoped(ActivityRetainedScope).singleton {
        (context as ComponentActivity).lifecycleScope
    }

    bindSingleton { AndroidSystemThemeDetector(instance()) }
    bindSingleton<SystemThemeDetector> { instance<AndroidSystemThemeDetector>() }
}
