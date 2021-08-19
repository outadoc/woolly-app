package fr.outadoc.woolly.android.inject

import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import fr.outadoc.woolly.common.inject.CommonDI
import fr.outadoc.woolly.ui.screen.AppScreenResources
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Copy
import org.kodein.di.android.ActivityRetainedScope
import org.kodein.di.bind
import org.kodein.di.bindSingleton
import org.kodein.di.scoped
import org.kodein.di.singleton
import org.kodein.di.subDI

val AndroidAppDI = subDI(CommonDI, copy = Copy.All) {

    bindSingleton { AppScreenResources() }

    bind<CoroutineScope>() with scoped(ActivityRetainedScope).singleton {
        (context as ComponentActivity).lifecycleScope
    }
}
